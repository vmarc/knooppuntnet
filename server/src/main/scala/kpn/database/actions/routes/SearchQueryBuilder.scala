package kpn.database.actions.routes

import kpn.api.common.search.Condition
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.ConditionGroupOperator.And
import kpn.api.common.search.ConditionGroupOperator.Or
import kpn.api.common.search.ConditionLocation
import kpn.api.common.search.ConditionName
import kpn.api.common.search.ConditionOperator.Equals
import kpn.api.common.search.ConditionTag
import kpn.core.doc.Label
import org.bson.conversions.Bson
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.or
import org.mongodb.scala.model.Filters.regex

object SearchQueryBuilder {

  def buildFilter(group: ConditionGroup): Bson = {
    val conditions: Seq[Bson] = group.conditions.map(buildCondition)
    group.operator match {
      case And => and(conditions: _*)
      case Or => or(conditions: _*)
    }
  }

  private def buildCondition(condition: Condition): Bson = {
    condition.tag match {
      case Some(tag) => buildConditionTag(tag)
      case None =>
        condition.location match {
          case Some(location) => buildConditionLocation(location)
          case None =>
            condition.name match {
              case Some(name) => buildConditionName(name)
              case None =>
                condition.group match {
                  case Some(group) => buildFilter(group)
                  case None => throw new IllegalArgumentException()
                }
            }
        }
    }
  }

  private def buildConditionTag(condition: ConditionTag): Bson = {
    condition.operator match {
      case Equals =>
        equal("summary.tags", BsonDocument(s"""{key: "${condition.key}", value: "${condition.value}"}"""))
      case _ =>
        and(
          equal("summary.tags.key", condition.key),
          regex("summary.tags.value", condition.pattern, "i")
        )
    }
  }

  private def buildConditionLocation(condition: ConditionLocation): Bson = {
    equal("labels", Label.location(condition.name))
  }

  private def buildConditionName(condition: ConditionName): Bson = {
    condition.operator match {
      case Equals =>
        equal("summary.name", condition.name)
      case _ =>
        regex("summary.name", condition.pattern, "i")
    }
  }
}
