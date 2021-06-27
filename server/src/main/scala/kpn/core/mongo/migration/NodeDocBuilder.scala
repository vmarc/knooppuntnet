package kpn.core.mongo.migration

import kpn.api.common.NodeInfo
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.ScopedNetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.NodeDoc
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

class NodeDocBuilder(database: Database) {

  def build(nodeInfo: NodeInfo): NodeDoc = {

    val routeReferences = database.routes.aggregate[Reference](routeReferencesPipeline(nodeInfo.id))
    val integrity = buildIntegrity(nodeInfo, routeReferences)
    val labels = buildLabels(nodeInfo, integrity, routeReferences)
    val  facts = nodeInfo.facts.filterNot(_.name == "IntegrityCheckFailed")

    NodeDoc(
      _id = nodeInfo.id,
      labels = labels,
      country = nodeInfo.country,
      name = nodeInfo.name,
      names = nodeInfo.names,
      latitude = nodeInfo.latitude,
      longitude = nodeInfo.longitude,
      lastUpdated = nodeInfo.lastUpdated,
      lastSurvey = nodeInfo.lastSurvey,
      tags = nodeInfo.tags,
      facts = facts,
      tiles = nodeInfo.tiles,
      locations = nodeInfo.locations,
      integrity = integrity,
      routeReferences = routeReferences
    )
  }

  private def buildLabels(nodeInfo: NodeInfo, integrity: Option[NodeIntegrity], routeReferences: Seq[Reference]): Seq[String] = {
    val basicLabels = buildBasicLabels(nodeInfo)
    val factLabels = nodeInfo.facts.map(fact => s"fact-${fact.name}")
    val networkTypeLabels = nodeInfo.names.map(name => s"network-type-${name.networkType.name}")
    val integrityCheckLabels = buildIntegrityCheckLabels(integrity)
    val locationLabels = nodeInfo.locations.map(location => s"location-$location")
    basicLabels ++ factLabels ++ networkTypeLabels ++ integrityCheckLabels ++ locationLabels
  }

  private def buildBasicLabels(nodeInfo: NodeInfo): Seq[String] = {
    Seq(
      if (nodeInfo.active) Some("active") else None,
      if (nodeInfo.orphan) Some("orphan") else None,
      if (nodeInfo.lastSurvey.isDefined) Some("survey") else None,
      if (nodeInfo.facts.nonEmpty) Some("facts") else None,
    ).flatten
  }

  private def buildIntegrity(nodeInfo: NodeInfo, routeReferences: Seq[Reference]): Option[NodeIntegrity] = {
    val nodeIntegrityDetails = ScopedNetworkType.all.flatMap { scopedNetworkType =>
      nodeInfo.tags(scopedNetworkType.expectedRouteRelationsTag) match {
        case None => None
        case Some(expectedRouteRelationsValue) =>
          if (expectedRouteRelationsValue.forall(Character.isDigit)) {
            val expectedRouteCount = expectedRouteRelationsValue.toInt
            val routeRefs = routeReferences.filter(rr => rr.networkType == scopedNetworkType.networkType && rr.networkScope == scopedNetworkType.networkScope).map(_.toRef)
            Some(
              NodeIntegrityDetail(
                scopedNetworkType.networkType,
                scopedNetworkType.networkScope,
                expectedRouteCount,
                routeRefs
              )
            )
          }
          else {
            None
          }
      }
    }
    if (nodeIntegrityDetails.nonEmpty) {
      Some(NodeIntegrity(nodeIntegrityDetails))
    }
    else {
      None
    }
  }

  private def buildIntegrityCheckLabels(nodeIntegrityOption: Option[NodeIntegrity]): Seq[String] = {
    nodeIntegrityOption match {
      case None => Seq.empty
      case Some(nodeIntegrity) =>
        val networkTypes = nodeIntegrity.details.map(_.networkType).distinct
        networkTypes.flatMap { networkType =>
          val failed = nodeIntegrity.details.filter(_.networkType == networkType).exists(_.failed)
          Seq(
            Some(s"integrity-check-${networkType.name}"),
            if (failed) Some(s"integrity-check-failed-${networkType.name}") else None
          ).flatten
        }
    }
  }

  private def routeReferencesPipeline(nodeId: Long): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("nodeRefs", nodeId)
        )
      ),
      project(
        fields(
          excludeId(),
          computed("networkType", "$summary.networkType"),
          computed("networkScope", "$summary.networkScope"),
          computed("id", "$summary.id"),
          computed("name", "$summary.name")
        )
      ),
      sort(orderBy(ascending("networkType", "networkScope", "routeName")))
    )
  }
}
