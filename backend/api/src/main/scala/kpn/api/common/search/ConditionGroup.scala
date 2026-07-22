package kpn.api.common.search

case class ConditionGroup(
  operator: ConditionGroupOperator,
  conditions: Seq[Condition]
)
