package kpn.core.replicate

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp

case class MinuteDiff(replicationId: ReplicationId, timestamp: Timestamp, xml: String)
