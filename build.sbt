name := "spider-app"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.2"

 val akkaVersion = "2.3.6"
 val luceneVersion = "4.7.0"
 val slickVersion = "2.1.0"
 val playSlickVersion = "0.8.0"
 val apacheCommonsVersion = "1.3.2"
 val mysqlVersion = "5.1.30"
 val mailerVersion = "2.3.0"
 val jbcryptVersion = "0.3m"
 val mokitoVersion = "1.9.5"
 val poiVersion =  "3.10-FINAL"
 val poiXmlVersion =  "3.9"
 val poiScratchpadVersion =  "3.9"
 val poiContribVersion = "3.6"
 val scalaTestVersion = "2.2.1"
 val junitVersion = "4.8.1"
 val scalaCheckVersion = "1.11.6"
 val scalaMockVersion = "3.2-RC1"
 val tikkaVersion = "1.6"
 val deadboltVersion = "2.3.2"

libraryDependencies ++= Seq( 
    javaCore,
    jdbc,
    anorm,
    cache,
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.play" %% "play-slick" % playSlickVersion,
    "com.typesafe.play.plugins" %% "play-plugins-mailer" % mailerVersion,
    "mysql" % "mysql-connector-java" % mysqlVersion,
    "org.mindrot" % "jbcrypt" % jbcryptVersion,
    "org.apache.poi" % "poi" % poiVersion,
    "org.apache.poi" % "poi-contrib" % poiContribVersion exclude("javax.jms", "jms") exclude("com.sun.jdmk", "jmxtools") exclude("com.sun.jmx", "jmxri"),
    "org.apache.poi" % "poi-ooxml" % poiXmlVersion,
    "org.apache.poi" % "poi-scratchpad" % poiScratchpadVersion,
    "org.apache.lucene" % "lucene-core" % luceneVersion withSources(),
    "org.apache.lucene" % "lucene-queries" % luceneVersion withSources(),
    "org.apache.lucene" % "lucene-queryparser" % luceneVersion withSources(),
    "org.apache.lucene" % "lucene-analyzers-common" % luceneVersion withSources(),
    "org.apache.lucene" % "lucene-facet" % luceneVersion withSources(),
    "org.apache.lucene" % "lucene-highlighter" % luceneVersion withSources(),
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "org.apache.commons" % "commons-io" % apacheCommonsVersion withSources(),
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "org.mockito" % "mockito-core" % mokitoVersion % "test",
    "junit" % "junit" % junitVersion % "test",
    "org.scalacheck" %% "scalacheck" % scalaCheckVersion % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockVersion % "test",
    "org.apache.tika" % "tika-core" % tikkaVersion,
    "org.apache.tika" % "tika-parsers" % tikkaVersion,
    "be.objectify" %% "deadbolt-scala" % deadboltVersion
  )
  
resolvers += Resolver.url("Objectify Play Repository", url("http://deadbolt.ws/releases/"))(Resolver.ivyStylePatterns)

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"
