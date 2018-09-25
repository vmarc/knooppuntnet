package kpn.shared.network

import kpn.shared.Fact
import kpn.shared.common.Ref

case class NetworkNodeFact(fact: Fact, nodes: Seq[Ref])
