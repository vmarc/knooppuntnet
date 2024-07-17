package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Node
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString

object RouteAnalysisFragmentGroup {
  private val geometryFactory = new GeometryFactory

  def apply(surface: String, fragments: Seq[RouteAnalysisFragment]): RouteAnalysisFragmentGroup = {
    val nodes: Seq[Node] = fragments.head.nodes ++ fragments.tail.flatMap(_.nodes.tail)
    val coordinates = nodes.map(node => new Coordinate(node.lon, node.lat)).toArray
    val nodeIds: Seq[Long] = nodes.map(_.id)
    val lineString = geometryFactory.createLineString(coordinates)
    RouteAnalysisFragmentGroup(surface, fragments, nodeIds, lineString)
  }
}

case class RouteAnalysisFragmentGroup(
  surface: String,
  fragments: Seq[RouteAnalysisFragment],
  nodeIds: Seq[Long],
  lineString: LineString
)
