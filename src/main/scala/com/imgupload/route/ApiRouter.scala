package com.imgupload.route

import java.io.ByteArrayInputStream
import org.json4s.DefaultJsonFormats
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import akka.actor.Actor
import akka.actor.ActorLogging
import spray.http.BodyPart
import spray.http.MultipartFormData
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol._
import spray.json._
import spray.routing.Directive.pimpApply
import spray.routing.Directives
import spray.routing.HttpService
import com.imgupload._
import spray.http.HttpHeaders._
import spray.http.StatusCodes
import spray.http.HttpEntity
import spray.http.HttpHeader
import spray.http.HttpHeaders
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import spray.http.MediaTypes

case class ImageUploaded(imgEndpoint: String, entity: HttpEntity, headers: Seq[HttpHeader] = Nil) {
  val name: Option[String] = dispositionParameterValue("name")

  val filename: Option[String] = dispositionParameterValue("filename")
  val fileByteArray = entity.data.toByteArray
  val s3FileName = s"${java.util.UUID.randomUUID.toString}_${filename.get}"
  val url = s"$imgEndpoint/$s3FileName"

  def dispositionParameterValue(parameter: String): Option[String] =
    headers.collectFirst {
      case HttpHeaders.`Content-Disposition`("form-data", parameters) if parameters.contains(parameter) ⇒
        parameters(parameter)
    }
}

class ApiRouter() extends Actor with ApiService with ActorLogging {

  def actorRefFactory = context

  def receive = runRoute(myRoute)
}

trait ApiService extends HttpService with Directives with DefaultJsonFormats {

  val bucketName = ServiceConfig.bucketName
  val imgEndpoint = ServiceConfig.imgEndpoint
  val AWS_ACCESS_KEY = ServiceConfig.AWS_ACCESS_KEY
  val AWS_SECRET_KEY = ServiceConfig.AWS_SECRET_KEY
  val sizeLimit = ServiceConfig.sizeLimit

  val yourAWSCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
  val amazonS3Client = new AmazonS3Client(yourAWSCredentials)

  val myRoute = {
    implicit val executionContext = actorRefFactory.dispatcher
    implicit val formats = org.json4s.DefaultFormats

    requestInstance { request =>
      pathEndOrSingleSlash {
        get {
          respondWithMediaType(MediaTypes.`application/json`) {
            complete(BuildInfo.toJson)
          }
        } ~ post {

          requestEntityPresent {

            headerValueByType[`Content-Length`]() { contentLength =>
              validate(contentLength.length / 1048576 <= sizeLimit, s"File size limit up to ${sizeLimit}MB.") {
                respondWithMediaType(MediaTypes.`application/json`) {
                  handleWith { formData: MultipartFormData =>

                    val image = formData.fields.map {
                      case BodyPart(entity, headers) =>
                        Try(ImageUploaded(imgEndpoint, entity, headers)).toOption
                    }.filter(_.isDefined).head.get

                    saveAttachment(image.s3FileName, new ByteArrayInputStream(image.fileByteArray)) match {
                      case Success(_) => s"""{"url":"${image.url}"}"""
                      case Failure(ex) => {
                        throw ex
                      }
                    }

                  }
                }
              }
            }
          }
        }
      }
    }
  }
  // $COVERAGE-OFF$
  def saveAttachment(fileName: String, content: ByteArrayInputStream) = Try(
    amazonS3Client.putObject(bucketName, fileName, content, null))
  // $COVERAGE-ON$
}


