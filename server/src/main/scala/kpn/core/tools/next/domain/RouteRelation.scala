package kpn.core.tools.next.domain

import kpn.api.custom.Relation

object RouteRelation {

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
      case Some(name) => name
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

    val relations = relation.relationMembers.filterNot(_.role.contains("place_of_worship")).map { member =>
      RouteRelation.from(member.relation, member.role)
    }

    RouteRelation(
      relationId = relation.id,
      name = name,
      role = role,
      relations = relations
    )
  }
}

case class RouteRelation(
  relationId: Long,
  name: String,
  role: Option[String],
  relations: Seq[RouteRelation]
)
