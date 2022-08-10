package kpn.server.repository

import kpn.api.common.common.Ref
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.springframework.stereotype.Component

case class NetworkFactElementIds(networkId: Long, networkName: String, elementIds: Seq[Long] = Seq.empty)

@Component
class FactRepositoryImpl(database: Database) extends FactRepository {

  private val log = Log(classOf[FactRepositoryImpl])

  override def factsPerNetwork(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    if (Fact.networkFactsWithElementIds.contains(fact)) {
      findNetworkFactsWithElementIds(subset, fact)
    }
    else if (Fact.networkFactsWithRefs.contains(fact)) {
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
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            equal("labels", Label.country(subset.country)),
            equal("labels", Label.networkType(subset.networkType)),
            equal("labels", Label.fact(fact)),
            // in addition to country label (labels can contain multiple countries):
            equal("summary.country", subset.country.domain),
          )
        ),
        project(
          fields(
            excludeId(),
            computed("id", "$_id"),
            computed("name", "$summary.name"),
          )
        )
      )
      val refs = database.routes.aggregate[Ref](pipeline, log)
      (s"routeRefs: ${refs.size}", refs)
    }
  }

  private def findNetworkRoutes(subset: Subset, routeIds: Seq[Long]): Seq[NetworkElement] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("country", subset.country.domain),
            equal("summary.networkType", subset.networkType.name)
          )
        ),
        unwind("$routes"),
        filter(in("routes.id", routeIds: _*)),
        project(
          fields(
            excludeId(),
            computed("networkId", "$_id"),
            computed("networkName", "$summary.name"),
            computed("elementId", "$routes.id"),
          )
        )
      )
      val references = database.networkInfos.aggregate[NetworkElement](pipeline, log)
      (s"route network references: ${references.size}", references)
    }
  }

  private def findNetworkFactsWithElementIds(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("country", subset.country.domain),
            equal("summary.networkType", subset.networkType.name)
          )
        ),
        unwind("$facts"),
        filter(equal("facts.name", fact.name)),
        project(
          fields(
            excludeId(),
            computed("networkId", "$_id"),
            computed("networkName", "$summary.name"),
            computed("elementIds", "$facts.elementIds"),
          )
        )
      )

      val elementReferences = database.networkInfos.aggregate[NetworkFactElementIds](pipeline, log)
      val references = elementReferences.map { reference =>
        NetworkFactRefs(
          reference.networkId,
          reference.networkName,
          reference.elementIds.map(id => Ref(id, id.toString))
        )
      }
      (s"network element references: ${references.size}", references)
    }
  }

  private def findNetworkFactsWithRefs(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("country", subset.country.domain),
            equal("summary.networkType", subset.networkType.name)
          )
        ),
        unwind("$facts"),
        filter(equal("facts.name", fact.name)),
        project(
          fields(
            excludeId(),
            computed("networkId", "$_id"),
            computed("networkName", "$summary.name"),
            computed("factRefs", "$facts.elements"),
          )
        )
      )

      val references = database.networkInfos.aggregate[NetworkFactRefs](pipeline, log)
      (s"network fact references: ${references.size}", references)
    }
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
    log.debugElapsed {

      val factLabel = s"integrity-check-failed-${subset.networkType.name}"

      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            equal("labels", Label.country(subset.country)),
            equal("labels", Label.networkType(subset.networkType)),
            equal("labels", factLabel),
          )
        ),
        unwind("$names"),
        filter(equal("names.networkType", subset.networkType.name)),
        project(
          fields(
            excludeId(),
            computed("id", "$_id"),
            computed("name", "$names.name"),
          )
        )
      )
      val refs = database.nodes.aggregate[Ref](pipeline, log)
      (s"nodeRefs: ${refs.size}", refs)
    }
  }

  private def findNetworkNodes(subset: Subset, nodeIds: Seq[Long]): Seq[NetworkElement] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("country", subset.country.domain),
            equal("summary.networkType", subset.networkType.name)
          )
        ),
        unwind("$nodes"),
        filter(in("nodes.id", nodeIds: _*)),
        project(
          fields(
            excludeId(),
            computed("networkId", "$_id"),
            computed("networkName", "$summary.name"),
            computed("elementId", "$nodes.id"),
          )
        )
      )
      val references = database.networkInfos.aggregate[NetworkElement](pipeline, log)
      (s"node network references: ${references.size}", references)
    }
  }
}
