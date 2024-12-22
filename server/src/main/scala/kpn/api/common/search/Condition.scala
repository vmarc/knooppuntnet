package kpn.api.common.search

case class Condition(
  conditionType: ConditionType,
  tag: Option[ConditionTag],
  location: Option[ConditionLocation],
  name: Option[ConditionRouteName],
  group: Option[ConditionGroup],
)
