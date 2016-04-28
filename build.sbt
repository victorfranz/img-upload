organization  := "com.imgupload"

name  := "img-upload"

version       := "0.1"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "SpringSource Milestone Repository" at "http://repo.springsource.org/milestone",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  val specs2V = "3.6"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-servlet"     % sprayV,
    "io.spray"            %%  "spray-io"     % sprayV,
    "io.spray"            %%  "spray-routing-shapeless2" % sprayV,
    "io.spray"            %%  "spray-client"  % sprayV,
    "io.spray"            %%  "spray-http"    % sprayV,
    "io.spray"            %%  "spray-httpx"   % sprayV,
    "io.spray"            %%  "spray-json"    % sprayV,
    "io.spray"            %%  "spray-caching"  % sprayV,
    "io.spray"            %%  "spray-util"  % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test" exclude("org.specs2", "specs2_2.11"),
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "com.typesafe"          % "config" % "1.2.1",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "com.googlecode.concurrentlinkedhashmap"  % "concurrentlinkedhashmap-lru"   % "1.4.2",
    "ch.qos.logback"        %  "logback-classic"   % "1.1.2",
    "org.json4s"          %% "json4s-jackson" % "3.2.11",
    "com.sksamuel.elastic4s"        %% "elastic4s"              % "1.5.5",
    "com.fasterxml.jackson.core"    %  "jackson-core"           %"2.4.1",
    "com.fasterxml.jackson.core"    %  "jackson-databind"       %"2.4.1",
    "com.fasterxml.jackson.module"  %% "jackson-module-scala"   % "2.4.1",
    "com.fasterxml.uuid"  % "java-uuid-generator"   % "3.1.3",
    "com.github.t3hnar" %% "scala-bcrypt" % "2.4",
    "org.json4s" %% "json4s-native" % "3.2.11",
    "org.json4s" %% "json4s-ext" % "3.2.11",
    "com.github.nscala-time" %% "nscala-time" % "1.8.0",
    "org.codehaus.groovy"            %  "groovy"                      % "2.3.7"  % "test",
    "org.specs2"          %%  "specs2-core"   % specs2V % "test",
    "org.specs2" %% "specs2-mock" % specs2V % "test",
    "org.specs2" %% "specs2-junit" % specs2V % "test",
    "org.specs2" %% "specs2-matcher-extra" % specs2V % "test",
    "org.specs2" %% "specs2-html" % specs2V % "test",
    "com.amazonaws" %   "aws-java-sdk"      %   "1.3.11"
  )
}

Revolver.settings

Revolver.enableDebugging(port = 5050, suspend = true)

tomcat()

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.imgupload",
    buildInfoOptions += BuildInfoOption.BuildTime,
    buildInfoOptions += BuildInfoOption.ToJson
  )
  