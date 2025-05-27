package kpn.database.actions.routes

import kpn.core.doc.NetworkRouteDetail
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryNetworkRouteDetails.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

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
      val routeDetails = database.baseRoutes.aggregate[NetworkRouteDetail](pipeline, log)
      (s"routeDetails: ${routeDetails.size}", routeDetails)
    }
  }

  private def buildPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
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
  }
}
