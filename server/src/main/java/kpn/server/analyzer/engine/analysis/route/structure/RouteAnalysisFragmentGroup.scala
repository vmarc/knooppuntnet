package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Node
import kpn.server.analyzer.engine.analysis.route.structure.RouteAnalysisFragmentGroup.geometryFactory
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString

object RouteAnalysisFragmentGroup {
  private val geometryFactory = new GeometryFactory
}

case class RouteAnalysisFragmentGroup(
  surface: String,
  fragments: Seq[RouteAnalysisFragment],
) {
  def nodes: Seq[Node] = fragments.head.nodes ++ fragments.tail.flatMap(_.nodes.tail)

  def nodeIds: Seq[Long] = nodes.map(_.id)

  def lineString: LineString = {
    val coordinates = nodes.map(node => new Coordinate(node.lon, node.lat)).toArray
    geometryFactory.createLineString(coordinates)
  }
}
