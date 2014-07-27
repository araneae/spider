name := "spider"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  javaCore,
  jdbc,
  anorm,
  cache,
  "com.typesafe.slick" %% "slick" % "2.0.1",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.typesafe" %% "play-plugins-mailer" % "2.1-RC2",
  "org.apache.poi" % "poi" % "3.10-FINAL",
  "org.apache.poi" % "poi-contrib" % "3.6" exclude("javax.jms", "jms") exclude("com.sun.jdmk", "jmxtools") exclude("com.sun.jmx", "jmxri"),
  "org.apache.poi" % "poi-ooxml" % "3.9",
  "org.apache.poi" % "poi-scratchpad" % "3.9",
  "org.apache.lucene" % "lucene-core" % "4.7.0",
  "org.apache.lucene" % "lucene-queries" % "4.7.0",
  "org.apache.lucene" % "lucene-queryparser" % "4.7.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "4.7.0",
  "org.apache.lucene" % "lucene-facet" % "4.7.0",
  "org.apache.lucene" % "lucene-highlighter" % "4.7.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.1.0",
  "junit" % "junit" % "4.8.1" % "test"
)

play.Project.playScalaSettings
