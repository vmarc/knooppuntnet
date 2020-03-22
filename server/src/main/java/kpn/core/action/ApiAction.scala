package kpn.core.action

case class ApiAction(
  user: Option[String],
  name: String,
  args: String,
  timestamp: ActionTimestamp,
  elapsed: Long
)
