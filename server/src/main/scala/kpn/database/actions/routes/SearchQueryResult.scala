package kpn.database.actions.routes

import kpn.api.common.data.Tagable
import kpn.api.custom.Tag

case class SearchQueryResult(
  _id: Long,
  tags: Seq[Tag]
) extends Tagable
