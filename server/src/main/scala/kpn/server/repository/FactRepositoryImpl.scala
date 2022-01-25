package kpn.server.repository

import kpn.api.common.common.Ref
import kpn.api.common.subset.NetworkFactRefs
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

@Component
class FactRepositoryImpl(database: Database) extends FactRepository {

  private val log = Log(classOf[FactRepositoryImpl])

  override def factsPerNetwork(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    routeFactsPerNetwork(subset, fact)
  }

  private def routeFactsPerNetwork(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {

    val routeRefs = findRoutesWithFact(subset, fact)
    val routeIds = routeRefs.map(_.id)
    val networkRoutes = findNetworkRoutes(subset, routeIds)

    val networkIds = networkRoutes.map(_.networkId).distinct.sorted
    val networkFactRefs = networkIds.map { networkId =>
      val networkName = networkRoutes.filter(_.networkId == networkId).head.networkName
      val networkRouteIds = networkRoutes.filter(_.networkId == networkId).map(_.routeId)
      val factRefs = routeRefs.filter(ref => networkRouteIds.contains(ref.id))
      NetworkFactRefs(
        networkId,
        networkName,
        factRefs
      )
    }

    val orphanRouteRefs = routeRefs.filter { ref =>
      !networkRoutes.exists(_.routeId == ref.id)
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

  private def findNetworkRoutes(subset: Subset, routeIds: Seq[Long]): Seq[NetworkRoute] = {
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
            computed("routeId", "$routes.id"),
          )
        )
      )
      val references = database.networkInfos.aggregate[NetworkRoute](pipeline, log)
      (s"route network references: ${references.size}", references)
    }
  }
}
