package kpn.server.repository

import kpn.api.common.Fact
import kpn.api.common.common.Ref
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Subset
import kpn.core.analysis.Facts
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQueryNetworkNodes
import kpn.database.actions.facts.MongoQueryNetworkRoutes
import kpn.database.actions.facts.MongoQueryNodesWithIntegrityCheckFailed
import kpn.database.actions.facts.MongoQueryRoutesWithFact
import kpn.database.actions.facts.MongoQuerySubsetNetworkFacts
import kpn.database.base.Database
import org.springframework.stereotype.Component

case class NetworkFactElementIds(networkId: Long, networkName: String, elementIds: Seq[Long] = Seq.empty)

@Component
class FactRepositoryImpl(database: Database) extends FactRepository {

  private val log = Log(classOf[FactRepositoryImpl])

  override def factsPerNetwork(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    if (Facts.networkFactsWithElementIds.contains(fact)) {
      findNetworkFactsWithElementIds(subset, fact)
    }
    else if (Facts.networkFactsWithRefs.contains(fact)) {
      findNetworkFactsWithRefs(subset, fact)
    }
    else if (Fact.IntegrityCheckFailed == fact) {
      findNetworkIntegrityCheckFailed(subset)
    }
    else {
      routeFactsPerNetwork(subset, fact)
    }
  }

  private def routeFactsPerNetwork(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {

    val routeRefs = findRoutesWithFact(subset, fact)
    val routeIds = routeRefs.map(_.id)
    val networkRoutes = findNetworkRoutes(subset, routeIds)

    val networkIds = networkRoutes.map(_.networkId).distinct.sorted
    val networkFactRefs = networkIds.map { networkId =>
      val networkName = networkRoutes.filter(_.networkId == networkId).head.networkName
      val networkRouteIds = networkRoutes.filter(_.networkId == networkId).map(_.elementId)
      val factRefs = routeRefs.filter(ref => networkRouteIds.contains(ref.id))
      NetworkFactRefs(
        networkId,
        networkName,
        factRefs
      )
    }.sortBy(_.networkName)

    val orphanRouteRefs = routeRefs.filter { ref =>
      !networkRoutes.exists(_.elementId == ref.id)
    }

    val allNetworkFactRefs = if (orphanRouteRefs.nonEmpty) {
      val nfr = NetworkFactRefs(
        0,
        "",
        orphanRouteRefs
      )
      networkFactRefs :+ nfr
    }
    else {
      networkFactRefs
    }

    allNetworkFactRefs
  }

  private def findRoutesWithFact(subset: Subset, fact: Fact): Seq[Ref] = {
    new MongoQueryRoutesWithFact(database).execute(subset, fact)
  }

  private def findNetworkRoutes(subset: Subset, routeIds: Seq[Long]): Seq[NetworkElement] = {
    new MongoQueryNetworkRoutes(database).execute(subset, routeIds)
  }

  private def findNetworkFactsWithElementIds(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    new MongoQuerySubsetNetworkFacts(database).execute(subset, fact)
  }

  private def findNetworkFactsWithRefs(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    new MongoQuerySubsetNetworkFacts(database).execute(subset, fact)
  }

  private def findNetworkIntegrityCheckFailed(subset: Subset): Seq[NetworkFactRefs] = {

    val nodeRefs = findNodesWithIntegrityCheckFailed(subset)
    val nodeIds = nodeRefs.map(_.id)
    val networkNodes = findNetworkNodes(subset, nodeIds)

    val networkIds = networkNodes.map(_.networkId).distinct.sorted
    val networkFactRefs = networkIds.map { networkId =>
      val networkName = networkNodes.filter(_.networkId == networkId).head.networkName
      val networkNodeIds = networkNodes.filter(_.networkId == networkId).map(_.elementId)
      val factRefs = nodeRefs.filter(ref => networkNodeIds.contains(ref.id)).sortBy(_.name)
      NetworkFactRefs(
        networkId,
        networkName,
        factRefs
      )
    }.sortBy(_.networkName)

    val orphanNodeRefs = nodeRefs.filter { ref =>
      !networkNodes.exists(_.elementId == ref.id)
    }

    val allNetworkFactRefs = if (orphanNodeRefs.nonEmpty) {
      val nfr = NetworkFactRefs(
        0,
        "",
        orphanNodeRefs
      )
      networkFactRefs :+ nfr
    }
    else {
      networkFactRefs
    }

    allNetworkFactRefs
  }

  private def findNodesWithIntegrityCheckFailed(subset: Subset): Seq[Ref] = {
    new MongoQueryNodesWithIntegrityCheckFailed(database).execute(subset)
  }

  private def findNetworkNodes(subset: Subset, nodeIds: Seq[Long]): Seq[NetworkElement] = {
    new MongoQueryNetworkNodes(database).execute(subset, nodeIds)
  }
}
