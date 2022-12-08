package kpn.api.common.monitor

import kpn.api.custom.Relation
import kpn.core.util.Haversine

object MonitorRouteRelation {

  def from(relation: Relation): MonitorRouteRelation = {

    val name = relation.tags("name")
    val from = relation.tags("from")
    val to = relation.tags("to")

    val osmWayCount = relation.wayMembers.size
    val osmDistance = relation.wayMembers.map(w => Haversine.meters(w.way.nodes)).sum

    val relations = relation.relationMembers.map { member =>
      MonitorRouteRelation.from(member.relation)
    }

    val subRelationsDistance = relations.map(_.osmDistance).sum

    MonitorRouteRelation(
      relationId = relation.id,
      name = name,
      from = from,
      to = to,
      deviationDistance = 0,
      deviationCount = 0,
      osmWayCount = osmWayCount,
      osmDistance = osmDistance + subRelationsDistance,
      osmSegmentCount = 0,
      happy = false,
      relations = relations
    )
  }
}

case class MonitorRouteRelation(
  relationId: Long,
  name: Option[String],
  from: Option[String],
  to: Option[String],
  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
  osmSegmentCount: Long,
  happy: Boolean,
  relations: Seq[MonitorRouteRelation]
)
