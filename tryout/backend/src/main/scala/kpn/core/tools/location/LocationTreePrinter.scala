package kpn.core.tools.location

import kpn.server.analyzer.engine.analysis.location.LocationTree

import java.io.PrintWriter

class LocationTreePrinter(out: PrintWriter) {
  def printTree(locations: Seq[LocationData], tree: LocationTree, level: Int = 0): Unit = {
    locations.find(_.id == tree.name).foreach { location =>
      val spaces = 1.to(level).map(_ => "  ").mkString
      val childCount = if (tree.children.isDefined) s" (${tree.children.get.size})" else ""
      out.println(s"$spaces - `${location.id}` ${location.name} $childCount")
      tree.childLocations.sortBy(_.name).foreach { child =>
        printTree(locations, child, level + 1)
      }
    }
  }
}
