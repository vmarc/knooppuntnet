package kpn.database.actions.routes

import kpn.api.common.search.Condition
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.ConditionGroupOperator
import kpn.api.common.search.ConditionLocation
import kpn.api.common.search.ConditionOperator
import kpn.api.common.search.ConditionRouteName
import kpn.api.common.search.ConditionSubject
import kpn.api.common.search.ConditionTag

object QueryBuilder {

  def andGroup(conditions: Condition*): ConditionGroup = {
    ConditionGroup(
      ConditionGroupOperator.And,
      conditions
    )
  }

  def orGroup(conditions: Condition*): ConditionGroup = {
    ConditionGroup(
      ConditionGroupOperator.Or,
      conditions
    )
  }

  def and(conditions: Condition*): Condition = {
    Condition(
      ConditionSubject.Group,
      group = Some(
        ConditionGroup(
          ConditionGroupOperator.And,
          conditions
        )
      )
    )
  }

  def or(conditions: Condition*): Condition = {
    Condition(
      ConditionSubject.Group,
      group = Some(
        ConditionGroup(
          ConditionGroupOperator.Or,
          conditions
        )
      )
    )
  }

  def tag(key: String, value: String): Condition = {
    Condition(
      ConditionSubject.Tag,
      tag = Some(
        ConditionTag(
          ConditionOperator.Equals,
          key,
          value
        )
      )
    )
  }

  def tagContains(key: String, value: String): Condition = {
    Condition(
      ConditionSubject.Tag,
      tag = Some(
        ConditionTag(
          ConditionOperator.Contains,
          key,
          value
        )
      )
    )
  }

  def tagStartsWith(key: String, value: String): Condition = {
    Condition(
      ConditionSubject.Tag,
      tag = Some(
        ConditionTag(
          ConditionOperator.StartsWith,
          key,
          value
        )
      )
    )
  }

  def tagEndsWith(key: String, value: String): Condition = {
    Condition(
      ConditionSubject.Tag,
      tag = Some(
        ConditionTag(
          ConditionOperator.EndsWith,
          key,
          value
        )
      )
    )
  }

  def name(value: String): Condition = {
    Condition(
      ConditionSubject.Name,
      name = Some(
        ConditionRouteName(
          ConditionOperator.Equals,
          value
        )
      )
    )
  }

  def nameContains(value: String): Condition = {
    Condition(
      ConditionSubject.Name,
      name = Some(
        ConditionRouteName(
          ConditionOperator.Contains,
          value
        )
      )
    )
  }

  def nameStartsWith(value: String): Condition = {
    Condition(
      ConditionSubject.Name,
      name = Some(
        ConditionRouteName(
          ConditionOperator.StartsWith,
          value
        )
      )
    )
  }

  def nameEndsWith(value: String): Condition = {
    Condition(
      ConditionSubject.Name,
      name = Some(
        ConditionRouteName(
          ConditionOperator.EndsWith,
          value
        )
      )
    )
  }

  def location(value: String): Condition = {
    Condition(
      ConditionSubject.Location,
      location = Some(
        ConditionLocation(
          ConditionOperator.Equals,
          value
        )
      )
    )
  }
}
