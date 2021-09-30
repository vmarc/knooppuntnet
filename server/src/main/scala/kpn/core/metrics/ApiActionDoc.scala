package kpn.core.metrics

import kpn.api.base.WithStringId

case class ApiActionDoc(_id: String, api: ApiAction) extends WithStringId
