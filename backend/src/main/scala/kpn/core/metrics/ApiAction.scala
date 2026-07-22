package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp
import kpn.api.id.Storable

case class ApiAction(
  remoteAddress: String,
  userAgentString: Option[String],
  deviceName: Option[String],
  deviceClass: Option[String],
  user: Option[String],
  name: String,
  args: String,
  timestamp: ActionTimestamp,
  elapsed: Long
) extends Storable
