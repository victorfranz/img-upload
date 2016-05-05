package com.imgupload.route

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import com.imgupload.ServiceConfig

@RunWith(classOf[JUnitRunner])
class PackageSpec extends Specification {

  "ServiceConfig" should {
    "load configuration from resources" >> {

      ServiceConfig.bucketName === "swap-images"
      ServiceConfig.imgEndpoint === "http://my-endpoint"
      ServiceConfig.AWS_ACCESS_KEY === "MY ACCESS KEY"
      ServiceConfig.AWS_SECRET_KEY === "MY SECRET KEY"
      ServiceConfig.sizeLimit === 2
    }
  }

}