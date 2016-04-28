package com.imgupload

import scala.concurrent.duration.DurationInt

import com.imgupload.route.ApiRouter

import akka.actor.ActorSystem
import akka.actor.Props
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

object Boot extends App {

  implicit val system = ActorSystem("imgupload")
  implicit val executionContext = system.dispatcher

  val apiRouter = system.actorOf(Props[ApiRouter], "ApiRouter")

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(apiRouter, interface = "0.0.0.0", port = 8080)

}