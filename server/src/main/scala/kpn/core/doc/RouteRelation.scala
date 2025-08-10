package kpn.core.doc

import kpn.api.custom.Relation

object RouteRelation {

  def relationIds(routeRelation: RouteRelation): Seq[Long] = {
    val subRelationIds = routeRelation.relations.toSeq.flatten.flatMap { subRouteRelation =>
      relationIds(subRouteRelation)
    }
    routeRelation.relationId +: subRelationIds
  }

  def from(relation: Relation, role: Option[String]): RouteRelation = {

    val nameTagKeys = Seq(
      "name",
      "name:de",
      "name:nl",
      "name:fr",
      "name:en",
      "ref",
    )

    val names = nameTagKeys.flatMap(nameTagKey => relation.tagValue(nameTagKey))

    val name = names.headOption match {
      case Some(routeName) => routeName
      case None =>
        relation.tagValue("from") match {
          case None => "?" // TODO get  name from 'name:fr', 'name:nl', etc.
          case Some(from) =>
            relation.tagValue("to") match {
              case None => "?"
              case Some(to) => s"$from — $to"
            }
        }
    }

    val relations = relation.relationMembers.filterNot(_.role.contains("place_of_worship")).flatMap { member =>
      member.relation.toSeq.map(relation => RouteRelation.from(relation, member.role))
    }

    RouteRelation(
      relationId = relation.id,
      name = name,
      role = role,
      relations = if (relations.nonEmpty) Some(relations) else None
    )
  }
}

case class RouteRelation(
  relationId: Long,
  name: String,
  role: Option[String],
  relations: Option[Seq[RouteRelation]]
)
