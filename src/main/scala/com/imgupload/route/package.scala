package com.imgupload

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.json4s.jackson.Serialization
import spray.http.MediaTypes
import spray.http.StatusCode
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.routing.ConjunctionMagnet.fromStandardRoute
import spray.routing.Directive.pimpApply
import spray.routing.Directives.complete
import spray.routing.Directives.parameters
import spray.routing.Directives.respondWithMediaType
import spray.routing.Directives.symbol2NR
import spray.routing.directives.ParamDefMagnet.apply
import com.typesafe.config.ConfigFactory

package object route {

  val ACKNOWLEDGED = "{\"acknowledged\" : true}"
  val REQUEST_ENTITY_EMPTY = "Request entity empty."
  val REQUEST_ENTITY_MALFORMED = "Malformed request entity."

}

object ServiceConfig {
  private val config = ConfigFactory.load()

  private lazy val root = config.getConfig("imgupload")

  lazy val bucketName = root.getString("bucketName")
  lazy val imgEndpoint = root.getString("imgEndpoint")
  lazy val AWS_ACCESS_KEY = root.getString("AWS_ACCESS_KEY")
  lazy val AWS_SECRET_KEY = root.getString("AWS_SECRET_KEY")
  lazy val sizeLimit = root.getInt("sizeLimit")
}