package kpn.core.doc

import kpn.api.common.common.Ref
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class NetworkInfoRouteDetail(
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
  tags: Seq[Tag],
  nodeRefs: Seq[Long]
) extends Tagable {

  def toRef: Ref = {
    Ref(id, name)
  }

  def isSameAs(other: NetworkInfoRouteDetail): Boolean = {
    name == other.name &&
      length == other.length &&
      role == other.role &&
      accessible == other.accessible &&
      roleConnection == other.roleConnection &&
      lastSurvey == other.lastSurvey &&
      proposed == other.proposed
  }
}
