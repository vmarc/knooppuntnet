package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.RouteType
import kpn.api.common.data.Way
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.footwayPaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.highwayUnpaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiSurfacePaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiSurfaceUnpaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiTracktypePaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiTracktypeUnpaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiUnpavedSmoothness

object SurfaceAnalyzer {

  // https://wiki.openstreetmap.org/wiki/Key:surface

  val wikiSurfacePaved: Seq[String] = Seq(
    "paved",
    "asphalt",
    "asphalt:lanes", // added
    "concrete",
    "concrete:lanes",
    "concrete:plates",
    "paving_stones",
    "paving_stones:lanes", // added
    "sett",
    "unhewn_cobblestone",
    "cobblestone",
    "cobblestone:flattened",
    "metal",
    "wood"
  )

  val wikiSurfaceUnpaved: Seq[String] = Seq(
    "unpaved",
    "compacted",
    "fine_gravel",
    "gravel",
    "pebblestone",
    "dirt",
    "earth",
    "grass",
    "grass_paver",
    "gravel_turf",
    "ground",
    "mud",
    "sand",
    "woodchips",
    "snow",
    "ice",
    "salt",
    "roots",
    // extra entries on top of wiki, based on openfietsmap:
    "shell",
    "shells",
    "ash",
    "bad",
    "clay",
    "cob",
    "loam",
    "soil",
    "rock",
    "turf"
  )

  // wiki smoothness values not used to determine paved/unpaved: excellent, good, intermediate, bad
  val wikiUnpavedSmoothness: Seq[String] = Seq(
    "very_bad",
    "horrible",
    "very_horrible",
    "impassable"
  )

  val wikiTracktypePaved: Seq[String] = Seq(
    "grade1"
  )

  val wikiTracktypeUnpaved: Seq[String] = Seq(
    "grade2",
    "grade3",
    "grade4",
    "grade5"
  )

  val highwayUnpaved: Seq[String] = Seq(
    "path",
    "bridleway",
    "track",
    "unsurfaced"
  )

  val footwayPaved: Seq[String] = Seq(
    "crossing",
    "traffic_island",
    "island",
    "sidewalk"
  )
}

class SurfaceAnalyzer(routeTypes: Seq[RouteType], way: Way) {

  def surface(): String = {
    surfaceBasedOnSurfaceTag() match {
      case Some(surface) => surface
      case None =>
        if (way.hasTag("tracktype", wikiTracktypePaved *)) {
          "paved"
        }
        else if (way.hasTag("tracktype", wikiTracktypeUnpaved *)) {
          "unpaved"
        }
        else if (way.hasTag("smoothness", wikiUnpavedSmoothness *)) {
          "unpaved"
        }
        else if (way.hasTag("highway", "footway")) {
          if (way.hasTag("footway", footwayPaved *)) {
            "paved"
          }
          else {
            "unknown"
          }
        }
        else if (way.hasTag("highway", "path")) {
          "unknown"
        }
        else if (way.hasTag("highway", highwayUnpaved *)) {
          "unpaved"
        }
        else {
          "paved"
        }
    }
  }

  private def surfaceBasedOnSurfaceTag(): Option[String] = {
    val tagKey = preferredSurfaceTagKey()
    if (way.hasTag(tagKey, wikiSurfacePaved *)) {
      Some("paved")
    }
    else if (way.hasTag(tagKey, wikiSurfaceUnpaved *)) {
      Some("unpaved")
    }
    else {
      None
    }
  }

  private def preferredSurfaceTagKey(): String = {
    if (routeTypes.contains(RouteType.hiking)) {
      if (way.hasTag("footway:surface")) {
        "footway:surface"
      }
      else if (way.hasTag("cycleway:surface")) {
        "cycleway:surface"
      }
      else {
        "surface"
      }
    }
    else if (routeTypes.contains(RouteType.cycling)) {
      if (way.hasTag("cycleway:surface")) {
        "cycleway:surface"
      }
      else {
        "surface"
      }
    }
    else {
      "surface"
    }
  }
}
