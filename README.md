# img-upload [![Build Status](https://travis-ci.org/victorfranz/img-upload.svg?branch=master)](https://travis-ci.org/victorfranz/img-upload) [![Codecov](https://img.shields.io/codecov/c/github/victorfranz/img-upload.svg?maxAge=2592000)](https://codecov.io/gh/victorfranz/img-upload) [![MIT licensed](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/victorfranz/img-upload/master/LICENSE)

A simple Spray service to upload images to S3 bucket

Configuration:
> src/main/resources/application.conf
> <pre>
imgupload {
    bucketName = "YOUR S3 BUCKET NAME"
    imgEndpoint = "YOUR S3 Static site endpoint or any other custom URL that provide access to your S3 files"
    AWS_ACCESS_KEY = "YOUR AWS_ACCESS_KEY"
    AWS_SECRET_KEY = "YOUR AWS_SECRET_KEY"
    sizeLimit = 2 // size limit in MB
}
</pre>

Run:
> sbt run

Example of usage:
> curl -i -F "data=@test.png" http://127.0.0.1:8080/
<pre>
< HTTP/1.1 200 OK
HTTP/1.1 200 OK
< Date: Thu, 28 Apr 2016 03:19:42 GMT
Date: Thu, 28 Apr 2016 03:19:42 GMT
< Content-Type: application/json; charset=UTF-8
Content-Type: application/json; charset=UTF-8
< Content-Length: 109
Content-Length: 109
 Connection #0 to host 127.0.0.1 left intact
{"url":"http://yourbucket.s3-website-sa-east-1.amazonaws.com/faef6e44-05ef-416c-a392-a8b388e4258b_test.png"}
</pre>


> curl http://127.0.0.1:8080/
<pre>
> GET / HTTP/1.1
> User-Agent: curl/7.35.0
> Host: 127.0.0.1:8080
> Accept: */*
> Referer: rbose
> 
< HTTP/1.1 200 OK
< Date: Thu, 28 Apr 2016 03:18:54 GMT
< Content-Type: application/json; charset=UTF-8
< Content-Length: 162
< 
* Connection #0 to host 127.0.0.1 left intact
{"builtAtMillis":"1461813488487", "name":"img-upload", "scalaVersion":"2.11.2", "version":"0.1", "sbtVersion":"0.13.6", "builtAtString":"2016-04-28 03:18:08.487"}
</pre>
