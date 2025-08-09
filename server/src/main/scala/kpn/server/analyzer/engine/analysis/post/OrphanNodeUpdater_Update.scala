package kpn.server.analyzer.engine.analysis.post

import com.mongodb.client.model.Accumulators.first
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.out
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.replaceRoot
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.core.doc.OrphanNodeDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.filter
import org.mongodb.scala.bson.BsonDocument

class OrphanNodeUpdater_Update(database: Database, log: Log) {

  def execute(allOrphanNodeIds: Seq[Long]): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in("_id", allOrphanNodeIds: _*)
        ),
        unwind("$names"),
        project(
          fields(
            excludeId(),
            BsonDocument("""{"_id": {"$concat": ["$country",":","$names.routeType",":", {"$toString": "$_id"}]}}"""),
            computed("country", "$country"),
            computed("routeType", "$names.routeType"),
            computed("nodeId", "$_id"),
            computed("name", "$names.name"),
            computed("longName", "$names.longName"),
            computed("proposed", "$names.proposed"),
            computed("lastUpdated", "$lastUpdated"),
            computed("lastSurvey", "$lastSurvey"),
            computed("facts", "$facts")
          )
        ),
        group(
          "$_id",
          /*
             Pick up first document only if there are multiple documents with the
             same network type but different network scopes (otherwise "duplicate key error"
             when inserting in orphanNodes collection).
          */
          first("firstResult", "$$ROOT")
        ),
        replaceRoot("$firstResult"),
        out(
          database.orphanNodes.name
        )
      )
      val orphanNodes = database.nodes.aggregate(pipeline, classOf[OrphanNodeDoc], log)
      (s"${orphanNodes.size} orphan nodes", ())
    }
  }
}
