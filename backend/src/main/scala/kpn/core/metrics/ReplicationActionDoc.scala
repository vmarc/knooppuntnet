package kpn.core.metrics

import kpn.api.id.WithStringId

case class ReplicationActionDoc(_id: String, replication: ReplicationAction) extends WithStringId
