package kpn.server.analyzer.engine.changes.changes

import kpn.api.common.ScopedNetworkType
import kpn.api.common.data.Way
import kpn.api.common.data._
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp

object RelationAnalyzer {
  def networkType(relation: RawRelation): Option[NetworkType] = {
    relation.tags("network").flatMap { tagValue =>
      ScopedNetworkType.all.find(_.key == tagValue).map(_.networkType)
    }
  }
}

trait RelationAnalyzer {

  def routeName(relation: Relation): String

  def toElementIds(relation: Relation): ElementIds

  def referencedNetworkNodes(relation: Relation): Set[Node]

  def referencedRoutes(relation: Relation): Set[Relation]

  def referencedNetworks(relation: Relation): Set[Relation]

  def referencedNodes(relation: Relation): Set[Node]

  def referencedNonConnectionNodes(relation: Relation): Set[Node]

  def referencedWays(relation: Relation): Set[Way]

  def referencedRelations(relation: Relation): Set[Relation]

  def lastUpdated(relation: Relation): Timestamp

  def waysLength(relation: Relation): Int

}
