package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Way
import kpn.api.custom.NetworkType
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
    "compact",
    "erde",
    "gr",
    "loam",
    "peb",
    "soil",
    "shotter",
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

class SurfaceAnalyzer(networkType: NetworkType, way: Way) {

  def surface(): String = {
    surfaceBasedOnSurfaceTag() match {
      case Some(surface) => surface
      case None =>
        if (way.tags.has("tracktype", wikiTracktypePaved: _*)) {
          "paved"
        }
        else if (way.tags.has("tracktype", wikiTracktypeUnpaved: _*)) {
          "unpaved"
        }
        else if (way.tags.has("smoothness", wikiUnpavedSmoothness: _*)) {
          "unpaved"
        }
        else if (way.tags.has("highway", "footway")) {
          if (way.tags.has("footway", footwayPaved: _*)) {
            "paved"
          }
          else {
            "unpaved"
          }
        }
        else if (way.tags.has("highway", "path")) {
          "unknown"
        }
        else if (way.tags.has("highway", highwayUnpaved: _*)) {
          "unpaved"
        }
        else {
          "paved"
        }
    }
  }

  private def surfaceBasedOnSurfaceTag(): Option[String] = {
    val tagKey = preferredSurfaceTagKey()
    if (way.tags.has(tagKey, wikiSurfacePaved: _*)) {
      Some("paved")
    }
    else if (way.tags.has(tagKey, wikiSurfaceUnpaved: _*)) {
      Some("unpaved")
    }
    else {
      None
    }
  }

  private def preferredSurfaceTagKey(): String = {
    if (networkType == NetworkType.hiking) {
      if (way.tags.has("footway:surface")) {
        "footway:surface"
      }
      else if (way.tags.has("cycleway:surface")) {
        "cycleway:surface"
      }
      else {
        "surface"
      }
    }
    else if (networkType == NetworkType.cycling) {
      if (way.tags.has("cycleway:surface")) {
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
