package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.data.MemberType
import kpn.core.doc.Label
import kpn.core.doc.NetworkInfoRouteDetail
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

@Component
class NetworkInfoRouteAnalyzer(database: Database) extends NetworkAnalyzer {

  private val log = Log(classOf[NetworkAnalysisContext])

  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    val routeIds = context.network.routeIds
    val routeDetails = queryRouteDetails(routeIds)
    val meters = routeDetails.map(_.length).sum
    val km = Math.round(meters.toDouble / 1000)
    val enrichedRouteDetails = routeDetails.map { networkRouteDetail =>
      val role = context.network.members.find(member => member.memberType == MemberType.Relation && member.ref == networkRouteDetail.id).flatMap(_.role) match {
        case Some(r) => Some(r)
        case None => None
      }
      val investigate = networkRouteDetail.facts.contains(Fact.RouteBroken)
      val accessible = !networkRouteDetail.facts.contains(Fact.RouteInaccessible)
      val roleConnection = role.contains("connection")
      networkRouteDetail.copy(
        role = role,
        investigate = investigate,
        accessible = accessible,
        roleConnection = roleConnection,
      )
    }
    val sortedRouteDetails = NaturalSorting.sortBy(enrichedRouteDetails)(_.name)
    val connectionCount = enrichedRouteDetails.count(_.roleConnection)

    context.copy(
      routeDetails = sortedRouteDetails,
      connectionCount = connectionCount,
      meters = meters,
      km = km
    )
  }

  private def queryRouteDetails(routeIds: Seq[Long]): Seq[NetworkInfoRouteDetail] = {
    if (routeIds.nonEmpty) {
      log.debugElapsed {
        val pipeline = Seq(
          filter(
            and(
              equal("labels", Label.active),
              in("_id", routeIds: _*)
            )
          ),
          project(
            fields(
              computed("id", "$_id"),
              computed("name", "$summary.name"),
              computed("length", "$summary.meters"),
              include("facts"),
              include("proposed"),
              include("lastUpdated"),
              include("lastSurvey"),
              computed("tags", "$summary.tags"),
              include("nodeRefs")
            )
          )
        )
        println(Mongo.pipelineString(pipeline))
        val routeDetails = database.baseRoutes.aggregate[NetworkInfoRouteDetail](pipeline, log)
        (s"routeDetails: ${routeDetails.size}", routeDetails)
      }
    }
    else {
      Seq.empty
    }
  }
}
