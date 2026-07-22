package kpn.server.analyzer.engine.changes

import kpn.api.common.data.raw.RawRelation

case class RawRelationChange(
  before: RawRelation,
  after: RawRelation
) {
  def id: Long = before.id
}
