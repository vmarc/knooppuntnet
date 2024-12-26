package kpn.api.common.search

case class Condition(
  subject: ConditionSubject,
  tag: Option[ConditionTag] = None,
  location: Option[ConditionLocation] = None,
  name: Option[ConditionRouteName] = None,
  group: Option[ConditionGroup] = None,
)
