package kpn.core.doc

import kpn.api.common.Relation
import kpn.server.analyzer.engine.tiles.domain.CoordinateCodec
import org.locationtech.jts.geom.Coordinate

object Detail {
  def from(relation: Relation): Detail = {
    val ways = relation.ways.map { way =>
      val cs = way.nodes.toArray.map(node => new Coordinate(node.lat, node.lon))
      val coordinates = CoordinateCodec.encode(cs)
      DetailWay(
        way.id,
        way.version,
        way.changeSetId,
        way.timestamp,
        way.tags,
        way.nodes.map(_.id),
        coordinates
      )
    }
    Detail(ways)
  }
}

case class Detail(
  ways: Seq[DetailWay]
) extends Storable
