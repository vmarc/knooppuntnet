package kpn.database.actions.routes

import kpn.api.common.data.Tagable
import kpn.api.custom.Tag
import kpn.api.id.Storable

case class SearchQueryResult(
  _id: Long,
  tags: Seq[Tag]
) extends Tagable with Storable
