package kpn.core.tools.location

import kpn.server.analyzer.engine.analysis.location.LocationTree

class NewLocationTreeBuilder {

  def buildTree(locations: Seq[LocationData]): LocationTree = {
    val pathMap = buildPathMap(locations)
    val tree = LocationTree("", Some(buildTrees(pathMap, "")))
    tree.childLocations.head
  }

  private def buildTrees(pathMap: Map[String, Seq[LocationData]], path: String): Seq[LocationTree] = {
    pathMap.getOrElse(path, Seq.empty).map { location =>
      val children = buildTrees(pathMap, path + ":" + location.id)
      if (children.nonEmpty) {
        LocationTree(location.id, Some(children))
      }
      else {
        LocationTree(location.id)
      }
    }
  }

  private def buildPathMap(locations: Seq[LocationData]): Map[String, Seq[LocationData]] = {
    locations.flatMap { location =>
      if (location.paths.nonEmpty) {
        location.paths.map { path =>
          val pathKey = path.locationIds.mkString(":", ":", "")
          pathKey -> location
        }
      }
      else {
        Seq("" -> location)
      }
    }.groupBy(_._1).map(x => x._1 -> x._2.map(_._2))
  }
}
