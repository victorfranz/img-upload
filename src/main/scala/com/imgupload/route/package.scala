package com.imgupload

import com.typesafe.config.ConfigFactory

object ServiceConfig {
  private val config = ConfigFactory.load()

  private lazy val root = config.getConfig("imgupload")

  lazy val bucketName = root.getString("bucketName")
  lazy val imgEndpoint = root.getString("imgEndpoint")
  lazy val AWS_ACCESS_KEY = root.getString("AWS_ACCESS_KEY")
  lazy val AWS_SECRET_KEY = root.getString("AWS_SECRET_KEY")
  lazy val sizeLimit = root.getInt("sizeLimit")
}