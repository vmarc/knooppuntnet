package kpn.api.common.search

case class Condition(
  subject: ConditionSubject,
  tag: Option[ConditionTag],
  location: Option[ConditionLocation],
  name: Option[ConditionRouteName],
  group: Option[ConditionGroup],
)
