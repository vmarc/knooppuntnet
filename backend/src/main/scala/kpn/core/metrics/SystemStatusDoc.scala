package kpn.core.metrics

import kpn.api.id.WithStringId

case class SystemStatusDoc(_id: String, status: SystemStatus) extends WithStringId
