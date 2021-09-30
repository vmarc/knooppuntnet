package kpn.core.metrics

import kpn.api.base.WithStringId

case class ReplicationActionDoc(_id: String, replication: ReplicationAction) extends WithStringId
