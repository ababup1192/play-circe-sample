name := """play-circe"""
organization := "org.ababup1192"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test,
  "io.circe" %% "circe-core" % "0.10.0",
  "io.circe" %% "circe-generic" % "0.10.0",
  "io.circe" %% "circe-parser" % "0.10.0",
  "io.tabmo" %% "circe-validation-core" % "0.0.6",
  "io.tabmo" %% "circe-validation-extra-rules" % "0.0.6",
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.ababup1192.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.ababup1192.binders._"
