package kpn.core.metrics

import kpn.api.base.WithStringId

case class UpdateActionDoc(_id: String, update: UpdateAction) extends WithStringId
