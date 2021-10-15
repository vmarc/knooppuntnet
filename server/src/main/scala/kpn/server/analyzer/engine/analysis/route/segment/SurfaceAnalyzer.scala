package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Way
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.highwayUnpaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiSurfacePaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiSurfaceUnpaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiTracktypePaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiTracktypeUnpaved
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer.wikiUnpavedSmoothness

object SurfaceAnalyzer {

  // https://wiki.openstreetmap.org/wiki/Key:surface

  val wikiSurfacePaved = Seq(
    "paved",
    "asphalt",
    "asphalt:lanes", // added
    "concrete",
    "concrete:lanes",
    "concrete:plates",
    "paving_stones",
    "paving_stones:30", // added
    "paving_stones:lanes", // added
    "sett",
    "unhewn_cobblestone",
    "cobblestone",
    "cobblestone:flattened",
    "metal",
    "wood"
  )

  val wikiSurfaceUnpaved = Seq(
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
    // extra entries on top of wiki, based on openfietsmap
    "shell", "shells", "ash", "bad", "clay", "cob", "compact", "erde", "gr", "loam", "peb", "soil", "shotter", "rock", "turf"
  )

  // wiki smoothness values not used to determine paved/unpaved: excellent, good, intermediate, bad
  val wikiUnpavedSmoothness = Seq(
    "very_bad",
    "horrible",
    "very_horrible",
    "impassable"
  )

  val wikiTracktypePaved = Seq(
    "grade1"
  )

  val wikiTracktypeUnpaved = Seq(
    "grade2",
    "grade3",
    "grade4",
    "grade5"
  )

  val highwayUnpaved = Seq(
    "path",
    "bridleway",
    "track",
    "unsurfaced",
    "footway"
  )
}

class SurfaceAnalyzer(way: Way) {

  def surface(): String = {

    if (way.tags.has("surface", wikiSurfacePaved: _*)) {
      "paved"
    }
    else if (way.tags.has("surface", wikiSurfaceUnpaved: _*)) {
      "unpaved"
    }
    else if (way.tags.has("tracktype", wikiTracktypePaved: _*)) {
      "paved"
    }
    else if (way.tags.has("tracktype", wikiTracktypeUnpaved: _*)) {
      "unpaved"
    }
    else if (way.tags.has("smoothness", wikiUnpavedSmoothness: _*)) {
      "unpaved"
    }
    else if (way.tags.has("highway", highwayUnpaved: _*)) {
      "unpaved"
    }
    else {
      "paved"
    }
  }
}
