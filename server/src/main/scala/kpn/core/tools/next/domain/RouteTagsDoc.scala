package kpn.core.tools.next.domain

import kpn.api.base.WithId
import kpn.api.custom.Tag

case class RouteTagsDoc(
  _id: Long,
  tags: Seq[Tag]
) extends WithId
