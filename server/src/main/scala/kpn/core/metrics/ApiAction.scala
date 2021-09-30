package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp

case class ApiAction(
  user: Option[String],
  name: String,
  args: String,
  timestamp: ActionTimestamp,
  elapsed: Long
)
