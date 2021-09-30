package kpn.core.metrics

import kpn.api.base.WithStringId

case class SystemStatusDoc(_id: String, status: SystemStatus) extends WithStringId
