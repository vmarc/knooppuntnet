package kpn.database.actions.routes

import kpn.api.common.search.Condition
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.ConditionGroupOperator.And
import kpn.api.common.search.ConditionGroupOperator.Or
import kpn.api.common.search.ConditionLocation
import kpn.api.common.search.ConditionOperator.Equals
import kpn.api.common.search.ConditionRouteName
import kpn.api.common.search.ConditionSubject.Tag
import kpn.api.common.search.ConditionTag

import java.util.regex.Pattern

object SearchQueryPostProcessor {

  def process(group: ConditionGroup, results: Seq[SearchQueryResult]): Seq[SearchQueryResult] = {
    results.filter { result =>
      evaluateGroup(group, result)
    }
  }

  private def evaluateGroup(group: ConditionGroup, result: SearchQueryResult): Boolean = {
    group.operator match {
      case And => group.conditions.forall(c => evaluateCondition(c, result))
      case Or => group.conditions.exists(c => evaluateCondition(c, result))
    }
  }

  private def evaluateCondition(condition: Condition, result: SearchQueryResult): Boolean = {
    condition.subject match {
      case Tag => evaluateConditionTag(condition.tag.get, result)
      case _ => true
      //  case Location => evaluateConditionLocation(condition.location.get)
      //  case Name => evaluateConditionName(condition.name.get)
      //  case Group => evaluateConditionGroup(condition.group.get)
    }
  }

  private def evaluateConditionTag(condition: ConditionTag, result: SearchQueryResult): Boolean = {
    condition.operator match {
      case Equals => true
      case _ =>
        result.tagValue(condition.key) match {
          case Some(value) =>
            val pattern = Pattern.compile(condition.pattern, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(value)
            matcher.matches()
          case None => false
        }
    }
  }

  private def evaluateConditionLocation(condition: ConditionLocation): Boolean = {
    true
  }

  private def evaluateConditionName(condition: ConditionRouteName): Boolean = {
    true
  }

  private def evaluateConditionGroup(condition: ConditionGroup): Boolean = {
    true
  }
}
