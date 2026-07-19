package kpn.api.common.search

case class Condition(
  tag: Option[ConditionTag] = None,
  location: Option[ConditionLocation] = None,
  name: Option[ConditionName] = None,
  group: Option[ConditionGroup] = None,
)
