package kpn.api.common.network

import kpn.api.common.Fact
import kpn.api.common.common.Ref

case class NetworkNodeFact(fact: Fact, nodes: Seq[Ref])
