package kpn.core.tools.location

import kpn.server.analyzer.engine.analysis.location.LocationTree

class NewLocationTreeBuilder {

  def buildTree(locations: Seq[LocationDoc]): LocationTree = {
    val pathMap = buildPathMap(locations)
    LocationTree("", Some(buildTrees(pathMap, "")))
  }

  private def buildTrees(pathMap: Map[String, Seq[LocationDoc]], path: String): Seq[LocationTree] = {
    pathMap.getOrElse(path, Seq.empty).map { location =>
      val children = buildTrees(pathMap, path + ":" + location._id)
      if (children.nonEmpty) {
        LocationTree(location._id, Some(children))
      }
      else {
        LocationTree(location._id)
      }
    }
  }

  private def buildPathMap(locations: Seq[LocationDoc]): Map[String, Seq[LocationDoc]] = {
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
