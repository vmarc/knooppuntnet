package kpn.api.common.network

import kpn.api.common.common.Ref
import kpn.api.custom.Fact

case class NetworkRouteFact(fact: Fact, routes: Seq[Ref])

