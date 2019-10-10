package kpn.shared.changes.details

case class NodeChangeDoc(_id: String, nodeChange: NodeChange, _rev: Option[String] = None)
