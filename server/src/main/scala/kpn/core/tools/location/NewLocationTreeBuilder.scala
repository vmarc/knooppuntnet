package kpn.core.tools.location

import kpn.core.doc.LocationDoc
import kpn.core.doc.LocationPath
import kpn.server.analyzer.engine.analysis.location.LocationTree

class NewLocationTreeBuilder {

  def buildTree(locations: Seq[LocationDoc]): Seq[LocationTree] = {
    buildTreeNode(locations: Seq[LocationDoc], LocationPath(Seq.empty))
  }

  private def buildTreeNode(locations: Seq[LocationDoc], path: LocationPath): Seq[LocationTree] = {
    locations.filter(_.matchesPath(path)).map { location =>
      val children = buildTreeNode(locations, LocationPath(path.locationIds :+ location._id))
      if (children.nonEmpty) {
        LocationTree(location._id, Some(children))
      }
      else {
        LocationTree(location._id)
      }
    }
  }
}
