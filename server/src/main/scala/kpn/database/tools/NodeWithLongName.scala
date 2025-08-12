package kpn.database.tools

import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class NodeWithLongName(id: Long, name: String, longName: String) extends Storable
