package kpn.core.doc

import kpn.api.common.common.Ref
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tags
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
  tags: Tags,
  nodeRefs: Seq[Long]
) {

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
