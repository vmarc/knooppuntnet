package kpn.core.tools.next.domain

import kpn.api.common.data.raw.RawWay

case class OldWay(raw: RawWay, nodes: Seq[OldNode], length: Long)
