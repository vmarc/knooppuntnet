package kpn.api.common.search

case class ConditionTag(
  operator: ConditionOperator,
  key: String,
  value: String,
)
