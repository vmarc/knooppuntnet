package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.data.MemberType
import kpn.core.doc.NetworkRouteDetail
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.database.base.Database
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
class NetworkRouteAnalyzer(database: Database) extends NetworkAnalyzer {

  private val log = Log(classOf[NetworkAnalysisContext])

  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    val routeIds = context.network.members.filter(_.memberType == MemberType.Relation).map(_.ref)
    val routeDetails = queryRouteDetails(routeIds)
    val meters = routeDetails.map(_.length).sum
    val km = Math.round(meters.toDouble / 1000)
    val enrichedRouteDetails = routeDetails.map { networkRouteDetail =>
      val role = context.network.members
        .find(member => member.memberType == MemberType.Relation && member.ref == networkRouteDetail.id)
        .flatMap(_.role)
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
      _routeDetails = Some(sortedRouteDetails),
      _connectionCount = Some(connectionCount),
      _meters = Some(meters),
      _km = Some(km)
    )
  }

  private def queryRouteDetails(routeIds: Seq[Long]): Seq[NetworkRouteDetail] = {
    if (routeIds.nonEmpty) {
      log.debugElapsed {
        val pipeline = Seq(
          filter(
            and(
              equal("active", true),
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
        val routeDetails = database.baseRoutes.aggregate[NetworkRouteDetail](pipeline, log)
        (s"routeDetails: ${routeDetails.size}", routeDetails)
      }
    }
    else {
      Seq.empty
    }
  }
}
