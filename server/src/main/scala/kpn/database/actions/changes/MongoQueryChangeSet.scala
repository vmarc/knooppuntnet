package kpn.database.actions.changes

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.core.util.Log
import kpn.database.actions.changes.MongoQueryChangeSet.log
import kpn.database.base.Database
import kpn.database.base.LongResult
import kpn.database.util.Mongo
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

object MongoQueryChangeSet {
  private val log = Log(classOf[MongoQueryChangeSet])
}

class MongoQueryChangeSet(database: Database) {

  def findReplicationIds(changeSetId: Long): Seq[Long] = {
    val pipeline = Seq(
      filter(equal("key.changeSetId", changeSetId)),
      project(
        fields(
          computed("value", "$key.replicationNumber")
        )
      )
    )

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val replicationNumbers = database.changes.aggregate[LongResult](pipeline).map(_.value)
      (s"${replicationNumbers.size} changeset replicationNumbers", replicationNumbers)
    }
  }

  def execute(changeSetId: Long, replicationId: ReplicationId): Option[ChangeSetData] = {
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

  private def findSummaries(changeSetId: Long, replicationId: ReplicationId): Option[ChangeSetSummary] = {

    val pipeline = Seq(
      filter(
        and(
          equal("key.changeSetId", changeSetId),
          equal("key.replicationNumber", replicationId.number)
        )
      )
    )

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val summary = database.changes.optionAggregate[ChangeSetSummary](pipeline)
      (s"changeset summary", summary)
    }
  }

  private def findNetworkChanges(changeSetId: Long, replicationNumber: Long): Seq[NetworkInfoChange] = {
    findChanges(changeSetId, replicationNumber) { pipeline =>
      val networkChanges = database.networkInfoChanges.aggregate[NetworkInfoChange](pipeline)
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
