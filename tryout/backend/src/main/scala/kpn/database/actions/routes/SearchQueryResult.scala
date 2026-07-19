package kpn.database.actions.routes

import kpn.api.common.data.Tagable
import kpn.api.custom.Tag
import kpn.core.doc.Storable

case class SearchQueryResult(
  _id: Long,
  tags: Seq[Tag]
) extends Tagable with Storable
