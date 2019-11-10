package kpn.api.common.network

import kpn.api.common.common.Ref
import kpn.api.custom.Fact

case class NetworkNodeFact(fact: Fact, nodes: Seq[Ref])
