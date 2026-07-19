package kpn.api.common.search;

import kpn.api.common.search.Condition;
import kpn.api.common.search.ConditionGroupOperator;

import com.google.common.collect.ImmutableList;

public record ConditionGroup(
  ConditionGroupOperator operator,
  ImmutableList<Condition> conditions
) {
}

/*
package kpn.api.common.search

case class ConditionGroup(
  operator: ConditionGroupOperator,
  conditions: Seq[Condition]
)

*/
