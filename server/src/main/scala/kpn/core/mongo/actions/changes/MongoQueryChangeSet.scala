package kpn.core.mongo.actions.changes

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.core.mongo.Database
import kpn.core.mongo.actions.changes.MongoQueryChangeSet.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal

object MongoQueryChangeSet {
  private val log = Log(classOf[MongoQueryChangeSet])
}

class MongoQueryChangeSet(database: Database) {

  def execute(changeSetId: Long, replicationId: Option[ReplicationId]): Seq[ChangeSetData] = {
    findSummaries(changeSetId, replicationId).map { changeSetSummary =>
      val replicationNumber = changeSetSummary.key.replicationNumber
      val networkChanges = findNetworkChanges(changeSetId, replicationNumber)
      val routeChanges = findRouteChanges(changeSetId, replicationNumber)
      val nodeChanges = findNodeChanges(changeSetId, replicationNumber)
      ChangeSetData(
        changeSetSummary,
        networkChanges,
        routeChanges,
        nodeChanges
      )
    }
  }

  private def findSummaries(changeSetId: Long, replicationId: Option[ReplicationId]): Seq[ChangeSetSummary] = {

    val filterElements = Seq(
      Some(equal("key.changeSetId", changeSetId)),
      replicationId.map(id => equal("key.replicationNumber", id.number))
    ).flatten

    val pipeline = Seq(
      filter(and(filterElements: _*))
    )

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val summaries = database.changeSetSummaries.aggregate[ChangeSetSummary](pipeline)
      (s"${summaries.size} changeset summaries", summaries)
    }
  }

  private def findNetworkChanges(changeSetId: Long, replicationNumber: Long): Seq[NetworkChange] = {
    findChanges(changeSetId, replicationNumber) { pipeline =>
      val networkChanges = database.networkChanges.aggregate[NetworkChange](pipeline)
      (s"${networkChanges.size} network changes", networkChanges)
    }
  }

  private def findRouteChanges(changeSetId: Long, replicationNumber: Long): Seq[RouteChange] = {
    findChanges(changeSetId, replicationNumber) { pipeline =>
      val routeChanges = database.routeChanges.aggregate[RouteChange](pipeline)
      (s"${routeChanges.size} route changes", routeChanges)
    }
  }

  private def findNodeChanges(changeSetId: Long, replicationNumber: Long): Seq[NodeChange] = {
    findChanges(changeSetId, replicationNumber) { pipeline =>
      val nodeChanges = database.nodeChanges.aggregate[NodeChange](pipeline)
      (s"${nodeChanges.size} node changes", nodeChanges)
    }
  }

  private def findChanges[T](changeSetId: Long, replicationNumber: Long)(ff: Seq[Bson] => (String, Seq[T])): Seq[T] = {
    val pipeline = Seq(
      filter(
        and(
          equal("key.changeSetId", changeSetId),
          equal("key.replicationNumber", replicationNumber)
        )
      )
    )
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    log.debugElapsed {
      ff(pipeline)
    }
  }
}
