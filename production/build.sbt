import sbt.Keys._
import sbt.Project.projectToRef

/*
  The 'shared' project contains classes, libraries and resources shared between the client and server.
 */
lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(
    scalaVersion := Settings.versions.scala,
    libraryDependencies ++= Settings.sharedDependencies.value
  )
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")

lazy val sharedJS = shared.js.settings(name := "sharedJS")

// use eliding to drop some debug code in the production build
lazy val elideOptions = settingKey[Seq[String]]("Set limit for elidable functions")


lazy val core: Project = (project in file("core"))
  .dependsOn(sharedJVM % "test->test;compile->compile")
  .settings(
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    //    test in assembly := {},
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"   % Settings.versions.akkaHttp,
      //"com.typesafe.akka" %% "akka-http-spray-json" % Settings.versions.akkaHttp,
      "com.typesafe.akka" %% "akka-actor"  % Settings.versions.akka,
      "com.typesafe.akka" %% "akka-stream" % Settings.versions.akka,
      "commons-lang" % "commons-lang" % "2.6",
      "commons-io" % "commons-io" % "2.4",
      "commons-net" % "commons-net" % "3.6",
      "com.github.scala-incubator.io" % "scala-io-file_2.11" % "0.4.3-1",
      "com.assembla.scala-incubator" % "graph-core_2.11" % "1.9.0",
      "org.scalatest" %% "scalatest" % Settings.versions.scalatest % "test",
      "org.scalamock" %% "scalamock-scalatest-support" % Settings.versions.scalamock % "test",
      "org.jsoup" % "jsoup" % "1.7.3" % "test",
      "io.spray" %% "spray-json" % "1.3.2",
      "io.spray" %% "spray-can" % "1.3.2",
      "io.spray" %% "spray-httpx" % "1.3.2",
      "io.spray" %% "spray-client" % "1.3.2",
      "com.github.scopt" %% "scopt" % "3.3.0",
      "org.apache.logging.log4j" % "log4j-api" % "2.11.0",
      "org.apache.logging.log4j" % "log4j-core" % "2.11.0",
      "jline" % "jline" % "2.12",
      "com.wdtinc" % "mapbox-vector-tile" % "1.1.1", // TODO dit gaat weg mogen ....
      "com.google.protobuf.nano" % "protobuf-javanano" % "3.0.0-alpha-2",
      "org.scala-lang.modules" %% "scala-pickling" % "0.10.1",
      ws
    )
  )

// instantiate the JS project for SBT with some additional settings
lazy val client: Project = (project in file("client"))
  .settings(
    name := "client",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    resolvers += Resolver.mavenLocal,
    libraryDependencies ++= Settings.scalajsDependencies.value,
    elideOptions := Seq(), // the default for development is to do no eliding
    scalacOptions ++= elideOptions.value,
    jsDependencies ++= Settings.jsDependencies.value,
    //jsDependencies += RuntimeDOM % "test", // RuntimeDOM is needed for tests
    skip in packageJSDependencies := false, // yes, we want to package JS dependencies
    scalaJSUseMainModuleInitializer := true,
    mainClass := Some("kpn.client.Main"),
    scalaJSUseMainModuleInitializer in Test := false,
    testFrameworks += new TestFramework("utest.runner.Framework")
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJS)

lazy val clients = Seq(client)

// instantiate the JVM project for SBT with some additional settings
lazy val server = (project in file("server"))
  .settings(
    name := "server",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    libraryDependencies ++= Settings.jvmDependencies.value,
      libraryDependencies ++= Seq(
      "com.vmunier" %% "scalajs-scripts" % "1.1.2",
      "commons-lang" % "commons-lang" % "2.6",
      "commons-io" % "commons-io" % "2.4",
      "com.github.scala-incubator.io" % "scala-io-file_2.11" % "0.4.3",
      "com.assembla.scala-incubator" % "graph-core_2.11" % "1.9.0",
      "org.scalatest" %% "scalatest" % Settings.versions.scalatest % "test",
      // "org.jsoup" % "jsoup" % "1.7.3" % "test",
      //      "com.typesafe.play" %% "play-mailer" % "2.4.1",
      "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2",
      filters, // GzipFilter
      "org.apache.logging.log4j" % "log4j-api" % "2.11.0",
      "org.apache.logging.log4j" % "log4j-core" % "2.11.0"
      //"org.mozilla" % "rhino" % "1.7.6"
    ),
    commands += ReleaseCmd,
    // connect to the client project
    scalaJSProjects := clients,
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    // compress CSS
    LessKeys.compress in Assets := true
  )
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin) // use the standard directory layout instead of Play's custom
  .aggregate(clients.map(projectToRef): _*)
  .dependsOn(core, sharedJVM)


// Command for building a release
lazy val ReleaseCmd = Command.command("release") {
  state => "set elideOptions in client := Seq(\"-Xelide-below\", \"WARNING\")" ::
    "client/clean" ::
    "client/test" ::
    "server/clean" ::
    "server/test" ::
    "server/dist" ::
    "set elideOptions in client := Seq()" ::
    state
}

// lazy val root = (project in file(".")).aggregate(client, server)

// loads the Play server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
