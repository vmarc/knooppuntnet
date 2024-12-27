package kpn.database.actions.routes

import kpn.api.common.search.Condition
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.ConditionGroupOperator.And
import kpn.api.common.search.ConditionGroupOperator.Or
import kpn.api.common.search.ConditionLocation
import kpn.api.common.search.ConditionOperator.Equals
import kpn.api.common.search.ConditionRouteName
import kpn.api.common.search.ConditionSubject.Group
import kpn.api.common.search.ConditionSubject.Location
import kpn.api.common.search.ConditionSubject.Name
import kpn.api.common.search.ConditionSubject.Tag
import kpn.api.common.search.ConditionTag
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
    condition.subject match {
      case Tag => buildConditionTag(condition.tag.get)
      case Location => buildConditionLocation(condition.location.get)
      case Name => buildConditionName(condition.name.get)
      case Group => buildConditionGroup(condition.group.get)
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
    equal("", "")
  }

  private def buildConditionName(condition: ConditionRouteName): Bson = {
    condition.operator match {
      case Equals =>
        equal("summary.name", condition.name)
      case _ =>
        regex("summary.name", condition.pattern, "i")
    }
  }

  private def buildConditionGroup(condition: ConditionGroup): Bson = {
    equal("", "")
  }
}
