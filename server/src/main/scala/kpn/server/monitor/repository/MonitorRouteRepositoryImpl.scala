package kpn.server.monitor.repository

import kpn.api.base.ObjectId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorRouteSegmentInfo
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.NameRow
import kpn.server.monitor.domain.MonitorGroupRouteCount
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.domain.OldMonitorRoute
import kpn.server.monitor.domain.OldMonitorRouteReference
import kpn.server.monitor.domain.OldMonitorRouteState
import kpn.server.repository.Distance
import kpn.server.repository.NetworkRepositoryImpl
import org.mongodb.scala.Document
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.or
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy
import org.springframework.stereotype.Component

@Component
class MonitorRouteRepositoryImpl(database: Database) extends MonitorRouteRepository {

  private val log = Log(classOf[NetworkRepositoryImpl])

  override def allRouteIds: Seq[Long] = {
    database.monitorRoutes.ids(log)
  }

  override def saveRoute(route: MonitorRoute): Unit = {
    database.monitorRoutes.save(route, log)
  }

  override def deleteRoute(routeId: ObjectId): Unit = {
    new MonitorRouteDelete(database).delete(routeId, log)
  }

  override def deleteRouteReferences(routeId: ObjectId): Unit = {
    val routeFilter = equal("routeId", routeId.raw)
    database.monitorRouteReferences.deleteMany(routeFilter, log)
  }

  override def deleteRouteReference(routeId: ObjectId, subRelationId: Long): Unit = {
    val routeReferenceFilter = and(
      equal("routeId", routeId.raw),
      equal("relationId", subRelationId)
    )
    database.monitorRouteReferences.deleteMany(routeReferenceFilter, log)
  }

  override def deleteRouteStates(routeId: ObjectId): Unit = {
    val routeFilter = equal("routeId", routeId.raw)
    database.monitorRouteStates.deleteMany(routeFilter, log)
  }

  override def deleteRouteState(routeId: ObjectId, subRelationId: Long): Unit = {
    val routeReferenceFilter = and(
      equal("routeId", routeId.raw),
      equal("relationId", subRelationId)
    )
    database.monitorRouteStates.deleteMany(routeReferenceFilter, log)
  }

  override def saveRouteState(routeState: MonitorRouteState): Unit = {
    database.monitorRouteStates.save(routeState, log)
  }

  override def saveRouteReference(routeReference: MonitorRouteReference): Unit = {
    database.monitorRouteReferences.save(routeReference, log)
  }

  override def saveRouteChange(routeChange: MonitorRouteChange): Unit = {
    database.monitorRouteChanges.save(routeChange, log)
  }

  override def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit = {
    database.monitorRouteChangeGeometries.save(routeChangeGeometry, log)
  }

  override def routeById(monitorRouteId: ObjectId): Option[MonitorRoute] = {
    database.monitorRoutes.findByObjectId(monitorRouteId, log)
  }

  override def routeByName(groupId: ObjectId, routeName: String): Option[MonitorRoute] = {
    val pipeline = Seq(
      filter(
        and(
          equal("groupId", groupId.raw),
          equal("name", routeName),
        )
      )
    )
    database.monitorRoutes.optionAggregate[MonitorRoute](pipeline, log)
  }

  override def oldRouteByName(groupId: ObjectId, routeName: String): Option[OldMonitorRoute] = {
    val pipeline = Seq(
      filter(
        and(
          equal("groupId", groupId.raw),
          equal("name", routeName),
        )
      )
    )
    database.oldMonitorRoutes.optionAggregate[OldMonitorRoute](pipeline, log)
  }

  override def routeState(routeId: ObjectId, relationId: Long): Option[MonitorRouteState] = {
    val pipeline = Seq(
      filter(
        and(
          equal("routeId", routeId.raw),
          equal("relationId", relationId),
        ),
      ),
    )
    database.monitorRouteStates.optionAggregate[MonitorRouteState](pipeline, log)
  }

  override def oldRouteState(routeId: ObjectId): Option[OldMonitorRouteState] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      sort(
        orderBy(
          descending(
            "timestamp"
          )
        )
      ),
      limit(1)
    )
    database.oldMonitorRouteStates.optionAggregate[OldMonitorRouteState](pipeline, log)
  }

  override def routeStates(routeId: ObjectId): Seq[MonitorRouteState] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      sort(
        orderBy(
          descending(
            "timestamp"
          )
        )
      )
    )
    database.monitorRouteStates.aggregate[MonitorRouteState](pipeline, log)
  }

  override def routeStateCount(routeId: ObjectId): Long = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      group(
        "routeId",
        sum("count", 1)
      ),
      project(
        fields(
          excludeId(),
          include("count"),
        )
      )
    )
    database.monitorRouteStates.aggregate[MonitorRouteCount](pipeline, log).map(_.count).sum
  }

  override def routeStateSize(routeId: ObjectId): Long = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      project(
        fields(
          include("routeId"),
          computed("size", Document("""{ $sum: { $bsonSize: "$$ROOT" } }"""))
        )
      ),
      group(
        "routeId",
        sum("count", "$size")
      ),
      project(
        fields(
          excludeId(),
          include("count"),
        )
      )
    )
    database.monitorRouteStates.aggregate[MonitorRouteCount](pipeline, log).map(_.count).sum
  }

  override def routeStateSegments(routeId: ObjectId): Seq[MonitorRouteSegmentInfo] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      unwind("$osmSegments"),
      project(
        fields(
          computed("id", "0"), // updated in Scala code below
          include("relationId"),
          computed("osmSegmentId", "$osmSegments.id"),
          computed("startNodeId", "$osmSegments.startNodeId"),
          computed("endNodeId", "$osmSegments.endNodeId"),
          computed("meters", "$osmSegments.meters"),
          computed("bounds", "$osmSegments.bounds"),
        )
      ),
      sort(
        orderBy(
          ascending(
            "relationId", "osmSegmentId"
          )
        )
      ),
    )

    val segments = database.monitorRouteStates.aggregate[MonitorRouteSegmentInfo](pipeline, log)
    segments.zipWithIndex.map { case (segment, index) =>
      segment.copy(id = index + 1)
    }
  }

  override def routeReference(routeId: ObjectId): Option[MonitorRouteReference] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      sort(
        orderBy(
          descending(
            "created"
          )
        )
      ),
      limit(1)
    )
    database.monitorRouteReferences.optionAggregate[MonitorRouteReference](pipeline, log)
  }

  override def routeRelationReference(routeId: ObjectId, relationId: Long): Option[MonitorRouteReference] = {
    val pipeline = Seq(
      filter(
        and(
          equal("routeId", routeId.raw),
          equal("relationId", relationId),
        )
      )
    )
    database.monitorRouteReferences.optionAggregate[MonitorRouteReference](pipeline, log)
  }

  override def routeReferences(routeId: ObjectId): Seq[MonitorRouteReference] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      )
    )
    database.monitorRouteReferences.aggregate[MonitorRouteReference](pipeline, log)
  }

  override def oldRouteReferenceRouteWithId(routeId: ObjectId): Option[OldMonitorRouteReference] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      sort(
        orderBy(
          descending(
            "created"
          )
        )
      ),
      limit(1)
    )
    database.oldMonitorRouteReferences.optionAggregate[OldMonitorRouteReference](pipeline, log)
  }

  override def superRouteReferenceSummary(routeId: ObjectId): Option[Long] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      project(
        fields(
          excludeId(),
          include("referenceDistance"),
        )
      ),
      group(
        1L,
        sum("referenceDistance", "$referenceDistance"),
      ),
    )

    database.monitorRouteReferences.optionAggregate[Distance](pipeline, log).map(_.referenceDistance)
  }

  override def superRouteStateSummary(routeId: ObjectId): Option[MonitorRouteStateSummary] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      project(
        fields(
          excludeId(),
          computed("deviationDistance", Document("""{ $sum: "$deviations.meters" }""")),
          computed("deviationCount", Document("""{ $size: "$deviations" }""")),
          include("wayCount"),
          include("osmDistance"),
        )
      ),
      group(
        1L,
        sum("deviationDistance", "$deviationDistance"),
        sum("deviationCount", "$deviationCount"),
        sum("osmWayCount", "$wayCount"),
        sum("osmDistance", "$osmDistance"),
      ),
    )

    database.monitorRouteStates.optionAggregate[MonitorRouteStateSummary](pipeline, log)
  }

  override def routeStateSummaries(routeId: ObjectId): Seq[MonitorRouteStateSummary] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      project(
        fields(
          excludeId(),
          include("relationId"),
          computed("deviationDistance", Document("""{ $sum: "$deviations.meters" }""")),
          computed("deviationCount", Document("""{ $size: "$deviations" }""")),
          computed("osmWayCount", "$wayCount"),
          include("osmDistance"),
          computed("osmSegmentCount", Document("""{ $size: "$osmSegments" }""")),
          include("startNodeId"),
          include("endNodeId"),
          include("happy"),
        )
      ),
    )

    database.monitorRouteStates.aggregate[MonitorRouteStateSummary](pipeline, log)
  }


  override def routeChange(changeKey: ChangeKey): Option[MonitorRouteChange] = {
    database.monitorRouteChanges.findOne(
      filter(
        and(
          equal("key.elementId", changeKey.elementId),
          equal("key.changeSetId", changeKey.changeSetId),
          equal("key.replicationNumber", changeKey.replicationNumber)
        )
      ),
      log
    )
  }

  override def routeChangeGeometry(changeKey: ChangeKey): Option[MonitorRouteChangeGeometry] = {
    database.monitorRouteChangeGeometries.findOne(
      filter(
        and(
          equal("key.elementId", changeKey.elementId),
          equal("key.changeSetId", changeKey.changeSetId),
          equal("key.replicationNumber", changeKey.replicationNumber)
        )
      ),
      log
    )
  }

  override def routeReferenceKey(routeMonitorId: String): Option[String] = {
    // TODO MONGO should be looking for most recent entry here, instead of assuming there is always exactly 1 entry ???
    database.monitorRouteReferences.findOne(
      filter(
        equal("routeId", routeMonitorId),
      ),
      log
    )
  }

  override def routeChange(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange] = {
    val _id = s"$monitorRouteId:$changeSetId:$replicationNumber"
    database.monitorRouteChanges.findByStringId(_id, log)
  }

  override def routeChangeGeometry(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry] = {
    val _id = s"$monitorRouteId:$changeSetId:$replicationNumber"
    database.monitorRouteChangeGeometries.findByStringId(_id, log)
  }

  override def changesCount(parameters: MonitorChangesParameters): Long = {
    if (parameters.impact) {
      val filter = or(
        equal("happy", true),
        equal("investigate", true)
      )
      database.monitorRouteChanges.countDocuments(
        filter,
        log
      )
    }
    else {
      database.monitorRouteChanges.countDocuments(log)
    }
  }

  override def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    val pipeline = Seq(
      if (parameters.impact) {
        Some(
          filter(
            or(
              equal("happy", true),
              equal("investigate", true),
            )
          )
        )
      }
      else {
        None
      },
      Some(sort(orderBy(descending("key.time")))),
      Some(skip((parameters.pageSize * parameters.pageIndex).toInt)),
      Some(limit(parameters.pageSize.toInt))
    ).flatten

    log.debugElapsed {
      val changes = database.monitorRouteChanges.aggregate[MonitorRouteChange](pipeline, log)
      val result = s"changes: ${changes.size}"
      (result, changes)
    }
  }

  override def groupRouteCounts(): Seq[MonitorGroupRouteCount] = {
    val pipeline = Seq(
      group(
        Document(
          "groupId" -> "$groupId"
        ),
        sum("routeCount", 1)
      ),
      project(
        fields(
          computed("groupId", "$_id.groupId"),
          include("routeCount"),
        )
      ),
    )
    database.monitorRoutes.aggregate[MonitorGroupRouteCount](pipeline, log)
  }

  override def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long = {
    val changesFilter = groupChangesFilter(groupName, parameters)
    database.monitorRouteChanges.countDocuments(changesFilter, log)
  }

  override def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    val pipeline = Seq(
      filter(
        groupChangesFilter(groupName, parameters)
      ),
      sort(orderBy(descending("key.time"))),
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt)
    )
    database.monitorRouteChanges.aggregate[MonitorRouteChange](pipeline, log)
  }

  override def routeChangesCount(id: String, parameters: MonitorChangesParameters): Long = {
    val changesFilter = routeChangesCountFilter(id, parameters)
    database.monitorRouteChanges.countDocuments(changesFilter, log)
  }

  override def routeChanges(monitorRouteId: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    val pipeline = Seq(
      filter(
        routeChangesCountFilter(monitorRouteId, parameters)
      ),
      sort(orderBy(descending("key.time"))),
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt)
    )
    database.monitorRouteChanges.aggregate[MonitorRouteChange](pipeline, log)
  }

  override def routeNames(groupId: ObjectId): Seq[String] = {
    val pipeline = Seq(
      filter(
        equal("groupId", groupId.raw)
      ),
      project(
        fields(
          include("name"),
        )
      )
    )
    database.monitorRoutes.aggregate[NameRow](pipeline, log).map(_.name)
  }

  private def groupChangesFilter(groupName: String, parameters: MonitorChangesParameters): Bson = {
    if (parameters.impact) {
      and(
        equal("groupName", groupName),
        or(
          equal("happy", true),
          equal("investigate", true)
        )
      )
    }
    else {
      equal("groupName", groupName)
    }
  }

  private def routeChangesCountFilter(monitorRouteId: String, parameters: MonitorChangesParameters): Bson = {
    if (parameters.impact) {
      and(
        equal("key.elementId", monitorRouteId),
        or(
          equal("happy", true),
          equal("investigate", true)
        )
      )
    }
    else {
      equal("key.elementId", monitorRouteId)
    }
  }

  override def routes(): Seq[MonitorRoute] = {
    database.monitorRoutes.findAll(log)
  }
}
