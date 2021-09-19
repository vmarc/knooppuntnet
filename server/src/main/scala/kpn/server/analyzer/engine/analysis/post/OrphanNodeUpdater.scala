package kpn.server.analyzer.engine.analysis.post

import kpn.core.mongo.Database
import kpn.core.mongo.doc.Label
import kpn.core.mongo.doc.OrphanNodeDoc
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
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.MergeOptions
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

object OrphanNodeUpdater {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new OrphanNodeUpdater(database).update()
    }
  }
}

@Component
class OrphanNodeUpdater(database: Database) {

  private val log = Log(classOf[OrphanNodeUpdater])

  def update(): Unit = {
    log.debugElapsed {
      val allNodeIds = findAllNodeIds()
      val nodeIdsReferencedInRoutes = findNodeIdsReferencedInRoutes()
      val nodeIdsReferencedInNetworks = findNodeIdsReferencedInNetworks()
      val nodeIds = (allNodeIds.toSet -- nodeIdsReferencedInRoutes -- nodeIdsReferencedInNetworks).toSeq.sorted
      updateOrphanNodes(nodeIds)
      ("done", ())
    }
  }

  private def findAllNodeIds(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            exists("country"),
            exists("names")
          )
        ),
        project(
          fields(
            include("_id")
          )
        )
      )
      val ids = database.nodes.aggregate[Id](pipeline, log)
      (s"${ids.size} nodes in total", ids.map(_._id))
    }
  }

  private def findNodeIdsReferencedInRoutes(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("labels", Label.active)
        ),
        unwind("$nodeRefs"),
        project(
          fields(
            computed("_id", "$nodeRefs")
          )
        )
      )
      val ids = database.routes.aggregate[Id](pipeline, log).map(_._id).distinct
      (s"${ids.size} nodes referenced in routes", ids)
    }
  }

  private def findNodeIdsReferencedInNetworks(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        unwind("$nodes"),
        project(
          fields(
            computed("_id", "$nodes.id")
          )
        )
      )
      val ids = database.networkInfos.aggregate[Id](pipeline, log).map(_._id).distinct
      (s"${ids.size} nodes referenced in networks", ids)
    }
  }

  private def updateOrphanNodes(nodeIds: Seq[Long]): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in("_id", nodeIds: _*)
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
        merge(
          database.orphanNodes.name,
          MergeOptions().uniqueIdentifier("_id")
        )
      )
      val orphanNodes = database.nodes.aggregate[OrphanNodeDoc](pipeline, log)
      (s"${orphanNodes.size} orphan nodes", ())
    }
  }
}
