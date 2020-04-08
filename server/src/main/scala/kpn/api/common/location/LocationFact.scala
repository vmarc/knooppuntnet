package kpn.api.common.location

import kpn.api.common.common.Ref
import kpn.api.custom.Fact

case class LocationFact(
  elementType: String,
  fact: Fact,
  refs: Seq[Ref]
)
