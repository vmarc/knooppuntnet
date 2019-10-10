package kpn.shared.network

import kpn.shared.Fact

import kpn.shared.common.Ref

case class NetworkRouteFact(fact: Fact, routes: Seq[Ref])

