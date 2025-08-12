package kpn.core.tools.support

import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class NodeImageTag(nodeId: Long, key: String, value: String) extends Storable
