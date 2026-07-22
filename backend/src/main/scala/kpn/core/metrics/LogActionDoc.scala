package kpn.core.metrics

import kpn.api.id.WithStringId

case class LogActionDoc(_id: String, log: LogAction) extends WithStringId
