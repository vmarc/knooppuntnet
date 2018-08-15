package kpn.core.load

import kpn.core.db.couch.Couch
import kpn.core.repository.RouteRepositoryImpl

import scala.io.Source

object TempAnalyzeLog extends App {

  findIgnoredNetworks()

  def findIgnoredNetworks(): Unit = {
    val lines = Source.fromFile("/home/marcv/tmp/kpn/logs/nov/no-nodes.log").getLines.toSeq
    val networkIds = lines.map(line => longStartingWith(line, "network=")).toSeq.distinct.sorted

    println("networkIds.size=" + networkIds.size)

    networkIds.foreach(println)
  }

  def findLongestMinuteDiffs(): Unit = {
    val lines = Source.fromFile("/home/marcv/tmp/kpn/logs/oct2/anal.log").getLines.toSeq
    println("minute diff count = " + lines.size)
    lines.foreach { line =>
      val millis = duration(line)
      if (millis > 50000) {
        println(line)
      }
    }
  }

  def totalGpxUpdateTime(): Unit = {
    val lines = Source.fromFile("/home/marcv/tmp/kpn/logs/oct2/gpx_update.log").getLines.toSeq
    println("minute diff count = " + lines.size)
    println("total gpx update time in minutes = " + lines.map(duration).sum / 1000 / 60)
  }

  def investigateRoutes(): Unit = {
    val lines = Source.fromFile("/home/marcv/tmp/kpn/logs/oct/analyzer-tool.log").getLines.filter(line => line.contains("OrphanRouteProcessorImpl"))
    val routeIds = lines.map(line => longStartingWith(line, "route=")).toSeq

    println("routeIds.size=" + routeIds.size)

    Couch.executeIn("master3") { database =>
      val repository = new RouteRepositoryImpl(database)
      routeIds.foreach { routeId =>
        repository.routeWithId(routeId) match {
          case None => println(s"$routeId not found")
          case Some(route) => println(s"$routeId ignored=${route.ignored} orphan=${route.orphan}")
        }
      }
    }
  }

  def analyzeOverpass(): Unit = {
    val lines = Source.fromFile("/home/marcv/tmp/kpn/logs/analyzer-tool-8.log").getLines.filter(line => line.contains("Overpass"))
    lines.foreach { line =>
      val millis = duration(line)
      if (millis > 30000) {
        if (network(line) != 33216L) {
          println(line)
        }
      }
    }
  }

  private def duration(line: String): Long = {
    longStartingWith(line, "(")
  }

  private def network(line: String): Long = {
    longStartingWith(line, "network=")
  }

  private def longStartingWith(line: String, prefix: String): Long = {
    val xxx = line.drop(line.indexOf(prefix) + prefix.length).takeWhile(_.isDigit)
    if (xxx.nonEmpty) {
      xxx.toLong
    }
    else {
      0
    }
  }
}
