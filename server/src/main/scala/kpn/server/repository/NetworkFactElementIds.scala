package kpn.server.repository

// TODO scala3 move back into using class
case class NetworkFactElementIds(networkId: Long, networkName: String, elementIds: Seq[Long] = Seq.empty)
