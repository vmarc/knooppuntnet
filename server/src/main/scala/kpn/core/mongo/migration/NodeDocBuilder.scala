package kpn.core.mongo.migration

import kpn.api.common.NodeInfo
import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.NodeDoc
import kpn.core.mongo.NodeRouteReference
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

    val routeReferences = database.routes.aggregate[NodeRouteReference](routeReferencesPipeline(nodeInfo.id))

    val labels = buildLabels(nodeInfo, routeReferences)

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
      facts = nodeInfo.facts,
      factCount = nodeInfo.facts.size, // TODO MONGO filter out Integrity facts ???
      tiles = nodeInfo.tiles,
      locations = nodeInfo.locations,
      routeReferences = routeReferences
    )
  }

  private def buildLabels(nodeInfo: NodeInfo, routeReferences: Seq[NodeRouteReference]): Seq[String] = {
    val basicLabels = buildBasicLabels(nodeInfo)
    val factLabels = nodeInfo.facts.map(fact => s"fact-${fact.name}")
    val networkTypeLabels = nodeInfo.names.map(name => s"network-type-${name.networkType.name}")
    val integrityCheckLabels = buildIntegrityCheckLabels(nodeInfo, routeReferences)
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

  private def buildIntegrityCheckLabels(nodeInfo: NodeInfo, routeReferences: Seq[NodeRouteReference]): Seq[String] = {
    NetworkType.all.flatMap { networkType =>
      val check = networkType.scopedNetworkTypes.exists { scopedNetworkType =>
        nodeInfo.tags.has(scopedNetworkType.expectedRouteRelationsTag)
      }

      val failed = networkType.scopedNetworkTypes.exists { scopedNetworkType =>
        nodeInfo.tags(scopedNetworkType.expectedRouteRelationsTag) match {
          case None => false
          case Some(expectedRouteRelationsValue) =>
            if (expectedRouteRelationsValue.forall(Character.isDigit)) {
              val expectedCount = expectedRouteRelationsValue.toInt
              val actualCount = routeReferences.count(rr => rr.networkType == networkType && rr.networkScope == scopedNetworkType.networkScope)
              expectedCount != actualCount
            }
            else {
              false
            }
        }
      }
      Seq(
        if (check) Some(s"integrity-check-${networkType.name}") else None,
        if (failed) Some(s"integrity-check-failed-${networkType.name}") else None
      ).flatten
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
          computed("routeId", "$summary.id"),
          computed("routeName", "$summary.name")
        )
      ),
      sort(orderBy(ascending("networkType", "networkScope", "routeName")))
    )
  }
}
