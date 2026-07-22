package kpn.core.metrics

import kpn.api.id.WithStringId

case class ApiActionDoc(_id: String, api: ApiAction) extends WithStringId
