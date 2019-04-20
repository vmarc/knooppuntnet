import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Settings {

  val name = "kpn"

  val version = "2.0.15-SNAPSHOT" // keep in sync: Version.current

  val scalacOptions = Seq(
    "-deprecation",
    "-encoding", "UTF-8", // these are 2 arguments
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
//    "-Ywarn-value-discard",  TODO temporarily disabled, try to re-enable again
    "-Xfuture",
    "-Xlint:-missing-interpolator"  // can probably remove again in play 2.4
  )

  /** Declare global dependency versions here to avoid mismatches in multi part dependencies */
  object versions {
    val scala = "2.11.12" // "2.12.3"

    val scalajs = "0.6.14" // TODO cleanup: not referenced ???
    val scalaDom = "0.9.3"
    val scalajsReact = "1.1.0"
    val scalaCss = "0.5.4"
    val scalajsReactComponents = "0.8.0-CUSTOM"  // see: cd wrk/soft/scalajs-react-components   ; git status   (sbt publishLocal)

    val akka = "2.5.12"
    val akkaHttp = "10.0.11"

    val autowire = "0.2.6"
    val booPickle = "1.2.5"
    val diode = "1.1.2"
    val uTest = "0.4.7"

    val react = "15.6.1"  // TODO cleanup: this setting seems not used
    val openlayers = "4.6.4"
    val marked = "0.3.2-1"
    val playScripts = "1.1.2"
    val scalatest = "3.0.1"
    val scalamock = "3.6.0"
  }

  /**
   * These dependencies are shared between JS and JVM projects
   * the special %%% function selects the correct version for each project
   */
  val sharedDependencies = Def.setting(
    Seq(
      "com.lihaoyi" %%% "autowire" % versions.autowire,
      "me.chrons" %%% "boopickle" % versions.booPickle,
      "org.scalatest" %%% "scalatest" % versions.scalatest % "test",
      "org.scalatest" %% "scalatest" % versions.scalatest % "test" // added to make tests runable from Intellij
    )
  )

  /** Dependencies only used by the JVM project */
  val jvmDependencies = Def.setting(
    Seq(
      "com.typesafe.play" %% "play-json" % "2.6.0",
      "org.scalatest" %% "scalatest" % versions.scalatest % "test"
    )
  )

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(
    Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % versions.scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalajsReact,
      "com.github.japgolly.scalacss" %%% "core" % versions.scalaCss,
      "com.olvind" %%% "scalajs-react-components" % versions.scalajsReactComponents,
      "io.suzaku" %%% "diode" % versions.diode,
      "io.suzaku" %%% "diode-react" % versions.diode,
      "org.scala-js" %%% "scalajs-dom" % versions.scalaDom,
      "com.github.karasiq" %%% "scalajs-marked" % "1.0.2",
      "org.scalatest" %%% "scalatest" % versions.scalatest % "test",
      "org.scalatest" %% "scalatest" % versions.scalatest % "test" // added to make tests in client module runable from Intellij
    )
  )

  val jsDependencies = Def.setting(
    Seq(
      "org.webjars" % "openlayers" % versions.openlayers / "ol-debug.js" minified "ol.js",
      "org.webjars" % "marked" % versions.marked / "marked.js"
    )
  )
}
