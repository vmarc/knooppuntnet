package kpn.api.common.changes.details

import kpn.core.database.doc.Doc

case class RouteChangeDoc(_id: String, routeChange: RouteChange, _rev: Option[String] = None) extends Doc
