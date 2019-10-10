package kpn.shared.route

import kpn.shared.Fact
import kpn.shared.RouteSummary
import kpn.shared.Timestamp
import kpn.shared.common.Ref
import kpn.shared.common.ToStringBuilder
import kpn.shared.data.Tagable
import kpn.shared.data.Tags

case class RouteInfo(
  summary: RouteSummary,
  active: Boolean,
  orphan: Boolean,
  version: Int,
  changeSetId: Long,
  lastUpdated: Timestamp,
  tags: Tags,
  facts: Seq[Fact],
  analysis: Option[RouteInfoAnalysis]
) extends Tagable {
  def id: Long = summary.id

  def toRef: Ref = Ref(summary.id, summary.name)

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("summary", summary).
    field("active", active).
    field("orphan", orphan).
    field("version", version).
    field("changeSetId", changeSetId).
    field("lastUpdated", lastUpdated).
    field("tags", tags).
    field("facts", facts).
    field("analysis", analysis).
    build
}
