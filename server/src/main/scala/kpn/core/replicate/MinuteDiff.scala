package kpn.core.replicate

import kpn.shared.ReplicationId
import kpn.shared.Timestamp

case class MinuteDiff(replicationId: ReplicationId, timestamp: Timestamp, xml: String)
