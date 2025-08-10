package kpn.database.actions.changes

import com.mongodb.client.model.Filters.and
import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.core.util.Log
import kpn.database.actions.changes.MongoQueryChangeSet.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo

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

    val conditions = Seq(
      Some(equal("key.changeSetId", changeSetId)),
      replicationId.map { rid =>
        equal("key.replicationNumber", rid.number)
      }
    ).flatten

    val pipeline = Seq(
      filter(
        and(conditions *)
      )
    )

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val summary = database.changes.aggregate(pipeline, classOf[ChangeSetSummary])
      (s"changeset summary", summary)
    }
  }

  private def findNetworkChanges(changeSetId: Long, replicationNumber: Long): Seq[NetworkChange] = {
    findChanges(changeSetId, replicationNumber) { pipeline =>
      val networkChanges = database.networkChanges.aggregate(pipeline, classOf[NetworkChange])
      (s"${networkChanges.size} network changes", networkChanges)
    }
  }

  private def findRouteChanges(changeSetId: Long, replicationNumber: Long): Seq[RouteChange] = {
    findChanges(changeSetId, replicationNumber) { pipeline =>
      val routeChanges = database.routeChanges.aggregate(pipeline, classOf[RouteChange])
      (s"${routeChanges.size} route changes", routeChanges)
    }
  }

  private def findNodeChanges(changeSetId: Long, replicationNumber: Long): Seq[NodeChange] = {
    findChanges(changeSetId, replicationNumber) { pipeline =>
      val nodeChanges = database.nodeChanges.aggregate(pipeline, classOf[NodeChange])
      (s"${nodeChanges.size} node changes", nodeChanges)
    }
  }

  private def findChanges[T](changeSetId: Long, replicationNumber: Long)(ff: MongoPipeline => (String, Seq[T])): Seq[T] = {
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
