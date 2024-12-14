package kpn.server.analyzer.engine.analysis.post

import kpn.core.doc.OrphanNodeDoc
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Accumulators.first
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.out
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.replaceRoot
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

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
            BsonDocument("""{"_id": {"$concat": ["$country",":","$names.networkType",":", {"$toString": "$_id"}]}}"""),
            computed("country", "$country"),
            computed("networkType", "$names.networkType"),
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
      val orphanNodes = database.nodes.aggregate[OrphanNodeDoc](pipeline, log)
      (s"${orphanNodes.size} orphan nodes", ())
    }
  }
}
