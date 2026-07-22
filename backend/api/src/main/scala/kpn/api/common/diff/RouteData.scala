package kpn.api.common.diff

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.common.data.Tagable
import kpn.api.common.data.raw.Raw
import kpn.api.common.route.RouteNode
import kpn.api.custom.Subset
import kpn.api.custom.Tag

case class RouteData(
  relationId: Long,
  raw: Raw,
  countries: Seq[Country],
  routeTypes: Seq[RouteType],
  name: String,
  networkNodes: Seq[RouteNode],
  facts: Seq[Fact],
  meters: Long
) extends Tagable {

  def toRef: Ref = Ref(relationId, name)

  def subsets: Seq[Subset] = {
    countries.flatMap { country =>
      routeTypes.flatMap { routeType =>
        Subset.of(country, routeType)
      }
    }
  }

  def tags: Seq[Tag] = raw.tags
}
