package kpn.core.metrics

import kpn.api.id.WithStringId

case class UpdateActionDoc(_id: String, update: UpdateAction) extends WithStringId
