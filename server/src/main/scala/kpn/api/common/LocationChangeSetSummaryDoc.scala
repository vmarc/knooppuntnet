package kpn.api.common

import kpn.core.database.doc.Doc

case class LocationChangeSetSummaryDoc(_id: String, locationChangeSetSummary: LocationChangeSetSummary, _rev: Option[String] = None) extends Doc
