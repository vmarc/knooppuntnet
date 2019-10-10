package kpn.core.engine.analysis.route.segment

import kpn.shared.data.Way


class SurfaceAnalyzer(way: Way) {

  // https://wiki.openstreetmap.org/wiki/Key:surface

  private val wikiSurfacePaved = Seq(
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

  private val wikiSurfaceUnpaved = Seq(
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
  private val wikiUnpavedSmoothness = Seq(
    "very_bad",
    "horrible",
    "very_horrible",
    "impassable"
  )

  private val wikiTracktypePaved = Seq(
    "grade1"
  )

  private val wikiTracktypeUnpaved = Seq(
    "grade2",
    "grade3",
    "grade4",
    "grade5"
  )

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
    else if (way.tags.has("highway", "bridleway", "track", "unsurfaced", "footway")) {
      "unpaved"
    }
    else if (way.tags.has("highway", "path")) {
      "unpaved"
    }
    else {
      "paved"
    }
  }

}
