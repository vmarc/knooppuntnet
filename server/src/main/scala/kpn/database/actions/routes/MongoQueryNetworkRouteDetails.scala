package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.doc.NetworkRouteDetail
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryNetworkRouteDetails.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryNetworkRouteDetails {
  private val log = Log(classOf[MongoQueryNetworkRouteDetails])
}

class MongoQueryNetworkRouteDetails(database: Database) {

  def execute(routeIds: Seq[Long]): Seq[NetworkRouteDetail] = {
    if (routeIds.nonEmpty) {
      executeQuery(routeIds)
    }
    else {
      Seq.empty
    }
  }

  private def executeQuery(routeIds: Seq[Long]): Seq[NetworkRouteDetail] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeIds)
      val routeDetails = database.baseRoutes.aggregate(pipeline, classOf[NetworkRouteDetail], log)
      (s"routeDetails: ${routeDetails.size}", routeDetails)
    }
  }

  private def buildPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          in("_id", routeIds *)
        )
      ),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$summary.name"),
          computed("length", "$summary.meters"),
          include("facts"),
          include("proposed"),
          include("lastUpdated"),
          include("lastSurvey"),
          computed("tags", "$summary.tags"),
          include("networkNodeIds")
        )
      )
    )
  }
}
