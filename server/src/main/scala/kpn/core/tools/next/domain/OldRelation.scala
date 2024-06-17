package kpn.core.tools.next.domain

import kpn.api.common.data.raw.RawRelation

case class OldRelation(
  raw: RawRelation,
  members: Seq[OldMember]
)
