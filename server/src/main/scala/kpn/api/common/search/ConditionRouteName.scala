package kpn.api.common.search

import kpn.api.common.search.ConditionOperator.Contains
import kpn.api.common.search.ConditionOperator.EndsWith
import kpn.api.common.search.ConditionOperator.Equals
import kpn.api.common.search.ConditionOperator.StartsWith

case class ConditionRouteName(
  operator: ConditionOperator,
  name: String,
) {
  def pattern: String = {
    operator match {
      case Equals => name
      case Contains => s".*$name.*"
      case StartsWith => s"^$name.*"
      case EndsWith => s".*$name$$"
    }
  }
}

