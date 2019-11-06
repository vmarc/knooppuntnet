package kpn.server.analyzer.engine.analysis.route

import kpn.shared.data.Way

object WayAnalyzer {

  def isRoundabout(way: Way): Boolean = way.tags.has("junction", "roundabout")

  def isClosedLoop(way: Way): Boolean = {
    way.nodes.size > 2 && way.nodes.head == way.nodes.last
  }

  def isSelfIntersecting(way: Way): Boolean = {
    way.nodes.size > 2 &&
      (way.nodes.toSet.size != way.nodes.size) &&
      way.nodes.head != way.nodes.last
  }

}
