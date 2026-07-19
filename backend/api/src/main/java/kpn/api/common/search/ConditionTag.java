package kpn.api.common.search;

import kpn.api.common.search.ConditionOperator;

public record ConditionTag(
  ConditionOperator operator,
  String key,
  String value
) {
}

/*
package kpn.api.common.search

import kpn.api.common.search.ConditionOperator.Contains
import kpn.api.common.search.ConditionOperator.EndsWith
import kpn.api.common.search.ConditionOperator.Equals
import kpn.api.common.search.ConditionOperator.StartsWith

case class ConditionTag(
  operator: ConditionOperator,
  key: String,
  value: String,
) {

  def pattern: String = {
    operator match {
      case Equals => value
      case Contains => s".*$value.*"
      case StartsWith => s"^$value.*"
      case EndsWith => s".*$value$$"
    }
  }
}

*/
