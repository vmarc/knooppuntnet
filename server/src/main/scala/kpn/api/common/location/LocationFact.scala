package kpn.api.common.location

import kpn.api.common.Fact
import kpn.api.common.common.Ref

case class LocationFact(
  elementType: String,
  fact: Fact,
  refs: Seq[Ref]
)
