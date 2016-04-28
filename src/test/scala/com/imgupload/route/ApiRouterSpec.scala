package com.imgupload.route

import java.io.ByteArrayInputStream
import java.io.File

import scala.concurrent.duration.DurationInt

import org.junit.runner.RunWith
import org.specs2.matcher.ValueCheck.typedValueCheck
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import akka.actor.ActorRefFactory
import akka.actor.ActorSystem
import akka.testkit.TestDuration
import spray.http.BodyPart
import spray.http.ContentType.apply
import spray.http.HttpHeaders
import spray.http.MediaTypes
import spray.http.MultipartFormData
import spray.http.StatusCodes.OK
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ApiRouterSpec extends Specification with Specs2RouteTest with Mockito with HttpService {
  def actorRefFactory = system
  val spec = this
  val contentTypeHeader = "Content-Type"
  val contentTypeHeaderValue = "application/json"
  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(30.second dilated)

  "ApiRouter" should {
    "upload an image" >> {
      "post" in {

        val img = new File(getClass.getResource("/test.png").getPath)
        val payload = MultipartFormData(Seq(BodyPart(img, "data", MediaTypes.`image/png`)))

        val service = createRoute()
        Post("/", payload) ~>
          addHeader(HttpHeaders.`Content-Length`(333)) ~>
          sealRoute(service) ~> check {

            val response = responseAs[String]
            response should contain("url", "test.png")

            status === OK
          }
      }
    }
    "return build info" >> {
      "get" in {

        val service = createRoute()
        Get("/") ~> sealRoute(service) ~> check {
          val response = responseAs[String]
          response should contain("builtAtMillis", """"name":"img-upload"""", "scalaVersion", """"version":"0.1"""", "builtAtString")
          status === OK

        }
      }
    }
  }

  def createRoute() = {
    new ApiService() {
      override implicit def actorRefFactory: ActorRefFactory = spec.actorRefFactory

      override def saveAttachment(fileName: String, content: ByteArrayInputStream) = scala.util.Try(null)
    }.myRoute
  }
}