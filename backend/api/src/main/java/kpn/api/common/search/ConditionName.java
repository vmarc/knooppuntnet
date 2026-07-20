package kpn.api.common.search;

import kpn.api.common.search.ConditionOperator;

public record ConditionName(
  ConditionOperator operator,
  String name
) {}

/* TODO migrate
package kpn.api.common.search

import kpn.api.common.search.ConditionOperator.Contains
import kpn.api.common.search.ConditionOperator.EndsWith
import kpn.api.common.search.ConditionOperator.Equals
import kpn.api.common.search.ConditionOperator.StartsWith

case class ConditionName(
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

*/
