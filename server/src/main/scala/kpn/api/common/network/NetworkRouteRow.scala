package kpn.api.common.network

import kpn.api.common.Fact
import kpn.api.common.common.Ref
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.doc.NetworkRouteDetail
import kpn.core.util.RouteSymbol

object NetworkRouteRow {
  def from(detail: NetworkRouteDetail): NetworkRouteRow = {
    val symbol = RouteSymbol.from(detail)
    NetworkRouteRow(
      detail.id,
      detail.name,
      detail.length,
      detail.role,
      detail.investigate,
      detail.accessible,
      detail.roleConnection,
      detail.lastUpdated,
      detail.lastSurvey,
      detail.proposed,
      detail.facts,
      symbol
    )
  }
}

case class NetworkRouteRow(
  id: Long,
  name: String,
  length: Long,
  role: Option[String],
  investigate: Boolean,
  accessible: Boolean,
  roleConnection: Boolean,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  proposed: Boolean,
  facts: Seq[Fact],
  symbol: Option[String]
) {

  def toRef: Ref = {
    Ref(id, name)
  }

  def isSameAs(other: NetworkRouteRow): Boolean = {
    name == other.name &&
      length == other.length &&
      role == other.role &&
      accessible == other.accessible &&
      roleConnection == other.roleConnection &&
      lastSurvey == other.lastSurvey &&
      proposed == other.proposed
  }
}
