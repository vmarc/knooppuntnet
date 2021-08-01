package kpn.server.analyzer.engine.analysis.post

import kpn.core.mongo.Database
import kpn.core.mongo.doc.OrphanRouteDoc
import kpn.core.mongo.util.Id
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.merge
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.MergeOptions
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

object OrphanRouteUpdater {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new OrphanRouteUpdater(database).update()
    }
  }
}

@Component
class OrphanRouteUpdater(database: Database) {

  private val log = Log(classOf[OrphanRouteUpdater])

  def update(): Unit = {
    log.debugElapsed {
      val allRouteIds = findAllRouteIds()
      val routeIdsReferencedInNetworks = findRouteIdsReferencedInNetworks()
      val routeIds = (allRouteIds.toSet -- routeIdsReferencedInNetworks).toSeq.sorted
      updateOrphanRoutes(routeIds)
      ("done", ())
    }
  }

  private def findAllRouteIds(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", "active"),
            BsonDocument("""{"summary.country": {"$exists": true}}""")
          )
        ),
        project(
          fields(
            include("_id")
          )
        )
      )
      val ids = database.routes.aggregate[Id](pipeline, log)
      (s"${ids.size} routes in total", ids.map(_._id))
    }
  }

  private def findRouteIdsReferencedInNetworks(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        unwind("$routes"),
        project(
          fields(
            computed("_id", "$routes.id")
          )
        )
      )
      val ids = database.networkInfos.aggregate[Id](pipeline, log).map(_._id).distinct
      (s"${ids.size} routes referenced in networks", ids)
    }
  }

  private def updateOrphanRoutes(routeIds: Seq[Long]): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in("_id", routeIds: _*)
        ),
        project(
          fields(
            computed("country", "$summary.country"),
            computed("networkType", "$summary.networkType"),
            computed("name", "$summary.name"),
            computed("meters", "$summary.meters"),
            computed("facts", "$facts"),
            computed("lastSurvey", "$lastSurvey"),
            computed("lastUpdated", "$lastUpdated")
          )
        ),
        merge(
          database.orphanRoutes.name,
          MergeOptions().uniqueIdentifier("_id")
        )
      )
      val orphanRoutes = database.routes.aggregate[OrphanRouteDoc](pipeline, log)
      (s"${orphanRoutes.size} orphan routes", ())
    }
  }
}
