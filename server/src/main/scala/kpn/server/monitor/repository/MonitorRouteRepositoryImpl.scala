package kpn.server.monitor.repository

import kpn.api.base.ObjectId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.core.util.Log
import kpn.database.actions.monitor.MongoQueryMonitorReferenceTileIds
import kpn.database.actions.monitor.MongoQueryMonitorReferenceTiles
import kpn.database.actions.monitor.MongoQueryMonitorStateTileIds
import kpn.database.actions.monitor.MongoQueryMonitorStateTiles
import kpn.database.base.Database
import kpn.database.base.NameRow
import kpn.database.base.ObjectIdId
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.domain.MonitorGroupRouteCount
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorReferenceTileInfo
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.OldMonitorReference
import kpn.server.repository.Distance
import kpn.server.repository.NetworkRepositoryImpl
import org.mongodb.scala.Document
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.BsonNull
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.or
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy
import org.springframework.stereotype.Component

@Component
class MonitorRouteRepositoryImpl(database: Database) extends MonitorRouteRepository {

  private val log = Log(classOf[NetworkRepositoryImpl])

  // *** MonitorRoute ***

  override def allRouteIds: Seq[Long] = {
    database.monitorRoutes.ids(log)
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

  override def saveRoute(route: MonitorRoute): Unit = {
    database.monitorRoutes.save(route, log)
  }

  override def deleteRoute(routeId: ObjectId): Unit = {
    new MonitorRouteDelete(database).delete(routeId, log)
  }

  // *** MonitorReference ***

  override def saveReference(reference: MonitorReference): Unit = {
    database.monitorReferences.save(reference, log)
  }

  override def deleteReferences(routeId: ObjectId): Unit = {
    val routeFilter = equal("routeId", routeId.raw)
    database.monitorReferences.deleteMany(routeFilter, log)
  }

  override def deleteReference(routeId: ObjectId, subRelationId: Long): Unit = {
    val routeReferenceFilter = and(
      equal("routeId", routeId.raw),
      equal("relationId", subRelationId)
    )
    database.monitorReferences.deleteMany(routeReferenceFilter, log)
  }

  override def deleteReferenceById(objectId: ObjectId): Unit = {
    database.monitorReferences.deleteByObjectId(objectId, log)
  }

  override def reference(routeId: ObjectId, relationId: Option[Long]): Option[MonitorReference] = {
    val relationIdValue = relationId match {
      case Some(value) => value
      case None => BsonNull()
    }
    val pipeline = Seq(
      routeReferenceFilter(routeId, relationId)
    )
    database.monitorReferences.optionAggregate[MonitorReference](pipeline, log)
  }

  override def routeRelationReferenceId(routeId: ObjectId, relationId: Option[Long]): Option[ObjectId] = {
    val pipeline = Seq(
      routeReferenceFilter(routeId, relationId),
      project(
        fields(
          include("_id")
        )
      )
    )
    database.monitorReferences.optionAggregate[ObjectIdId](pipeline, log).map(_._id)
  }

  private def routeReferenceFilter(routeId: ObjectId, relationId: Option[Long]): Bson = {
    val relationIdValue = relationId match {
      case Some(value) => value
      case None => BsonNull()
    }
    filter(
      and(
        equal("routeId", routeId.raw),
        equal("relationId", relationIdValue),
      )
    )
  }

  override def references(routeId: ObjectId): Seq[MonitorReference] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      )
    )
    database.monitorReferences.aggregate[MonitorReference](pipeline, log)
  }

  override def oldReferences(routeId: ObjectId): Seq[OldMonitorReference] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      )
    )
    database.oldMonitorReferences.aggregate[OldMonitorReference](pipeline, log)
  }

  override def referenceIds(routeId: ObjectId): Seq[MonitorReferenceId] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      project(
        fields(
          include("relationId"),
        )
      ),
    )

    database.monitorReferences.aggregate[MonitorReferenceId](pipeline, log)
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

    database.monitorReferences.optionAggregate[Distance](pipeline, log).map(_.referenceDistance)
  }
  // *** MonitorState ***

  override def state(routeId: ObjectId, relationId: Long): Option[MonitorState] = {
    val pipeline = Seq(
      filter(
        and(
          equal("routeId", routeId.raw),
          equal("relationId", relationId),
        ),
      ),
    )
    database.monitorStates.optionAggregate[MonitorState](pipeline, log)
  }

  override def stateTiles(routeId: ObjectId, relationId: Long): Seq[MonitorStateTile] = {
    val pipeline = Seq(
      filter(
        and(
          equal("routeId", routeId.raw),
          equal("relationId", relationId),
        ),
      ),
    )
    database.monitorStateTiles.aggregate[MonitorStateTile](pipeline, log)
  }

  override def states(routeId: ObjectId): Seq[MonitorState] = {
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
    database.monitorStates.aggregate[MonitorState](pipeline, log)
  }

  override def saveState(state: MonitorState): Unit = {
    database.monitorStates.save(state, log)
  }

  override def saveStateTile(stateTile: MonitorStateTile): Unit = {
    database.monitorStateTiles.save(stateTile, log)
  }

  override def deleteStates(routeId: ObjectId): Unit = {
    val routeFilter = equal("routeId", routeId.raw)
    database.monitorStates.deleteMany(routeFilter, log)
  }

  override def deleteStateTiles(routeId: ObjectId): Unit = {
    val routeFilter = equal("routeId", routeId.raw)
    database.monitorStateTiles.deleteMany(routeFilter, log)
  }

  override def deleteState(routeId: ObjectId, subRelationId: Long): Unit = {
    val routeReferenceFilter = and(
      equal("routeId", routeId.raw),
      equal("relationId", subRelationId)
    )
    database.monitorStates.deleteMany(routeReferenceFilter, log)
  }

  override def deleteStateById(objectId: ObjectId): Unit = {
    database.monitorStates.deleteByObjectId(objectId, log)
  }

  override def stateCount(routeId: ObjectId): Long = {
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
    database.monitorStates.aggregate[MonitorRouteCount](pipeline, log).map(_.count).sum
  }

  override def stateSize(routeId: ObjectId): Long = {
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
    database.monitorStates.aggregate[MonitorRouteCount](pipeline, log).map(_.count).sum
  }

  override def superRouteStateSummary(routeId: ObjectId): Option[MonitorStateSummary] = {
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

    database.monitorStates.optionAggregate[MonitorStateSummary](pipeline, log)
  }

  override def stateSummaries(routeId: ObjectId): Seq[MonitorStateSummary] = {
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
        )
      ),
    )

    database.monitorStates.aggregate[MonitorStateSummary](pipeline, log)
  }

  override def stateIds(routeId: ObjectId): Seq[MonitorStateId] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      ),
      project(
        fields(
          include("relationId"),
        )
      ),
    )

    database.monitorStates.aggregate[MonitorStateId](pipeline, log)
  }

  // *** changes ***

  override def saveRouteChange(routeChange: MonitorRouteChange): Unit = {
    database.monitorRouteChanges.save(routeChange, log)
  }

  override def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit = {
    database.monitorRouteChangeGeometries.save(routeChangeGeometry, log)
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
    database.monitorReferences.findOne(
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
      database.monitorRouteChanges.countFilteredDocuments(
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
      Option.when(parameters.impact) {
        filter(
          or(
            equal("happy", true),
            equal("investigate", true),
          )
        )
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

  override def groupRouteDetails(groupId: ObjectId): Seq[MonitorRouteDetail] = {
    val pipeline = Seq(
      filter(
        equal("groupId", groupId.raw)
      ),
      project(
        fields(
          excludeId(),
          BsonDocument("""{"routeId": {"$toString": "$_id"}}"""),
          include("name"),
          include("description"),
          include("symbol"),
          include("relationId"),
          include("referenceType"),
          include("referenceTimestamp"),
          include("referenceDistance"),
          include("deviationDistance"),
          include("deviationCount"),
          include("osmSegmentCount"),
          include("relationIds"),
          include("bounds"),
          include("happy"),
        )
      )
    )
    database.monitorRoutes.aggregate[MonitorRouteDetail](pipeline, log)
  }

  override def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long = {
    val changesFilter = groupChangesFilter(groupName, parameters)
    database.monitorRouteChanges.countFilteredDocuments(changesFilter, log)
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
    database.monitorRouteChanges.countFilteredDocuments(changesFilter, log)
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

  override def stateTileIds(): Seq[TileId] = {
    new MongoQueryMonitorStateTileIds(database).execute()
  }

  def referenceTileIds(): Seq[TileId] = {
    new MongoQueryMonitorReferenceTileIds(database).execute()
  }

  override def stateTiles(tileId: TileId): Seq[MonitorStateTile] = {
    new MongoQueryMonitorStateTiles(database).execute(tileId)
  }

  override def referenceTiles(tileId: TileId): Seq[MonitorReferenceTileInfo] = {
    new MongoQueryMonitorReferenceTiles(database).execute(tileId)
  }
}
