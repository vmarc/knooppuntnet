package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.analyzers.RouteWayTypeAnalyzer.prefixes

object RouteWayTypeAnalyzer {
  private val log = Log(classOf[RouteWayTypeAnalyzer])

  // https://wiki.openstreetmap.org/wiki/Lifecycle_prefix
  private val lifeCyclePrefixes: Seq[String] = Seq(
    "proposed",
    "planned",
    "construction",
    "disused",
    "abandoned",
    "ruins",
    "demolished",
    "removed",
    "razed",
    "destroyed",
    "was",
    "ruined",
    "closed",
    "no",
    "not",
  )

  private val extraPrefixes = Seq(
    "area"
  )

  val prefixes: Seq[String] = lifeCyclePrefixes ++ extraPrefixes
}

class RouteWayTypeAnalyzer {

  def analyze(member: WayMember): Option[String] = {
    val way = member.way
    if (way.tags.isEmpty) {
      None
    }
    else {
      highway(way) match {
        case Some(value) => Some(value)
        case None =>
          way.tagValue("waterway") match {
            case Some(value) => Some(value)
            case None =>
              way.tagValue("route") match {
                case Some("ferry") => Some("ferry")
                case _ =>
                  if (way.hasTag("railway")) {
                    way.tagValue("railway").map(value => s"railway $value")
                  }
                  else {
                    None
                  }
              }
          }
      }
    }
  }

  private def highway(way: Way): Option[String] = {
    way.tagValue("highway") match {
      case Some(value) =>
        Some(value)
      case None =>
        prefixes.find(prefix => way.hasTag(s"$prefix:highway")) match {
          case Some(prefix) => way.tagValue(s"$prefix:highway").map(value => s"$prefix $value")
          case None => None
        }
    }
  }
}
