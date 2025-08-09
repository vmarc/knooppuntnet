package kpn.server.monitor.repository

import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.skip
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.or
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.base.ObjectId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.api.common.monitor.MonitorRouteDeviationInfo
import kpn.api.common.monitor.MonitorRouteRelationInfo
import kpn.core.util.Log
import kpn.database.actions.monitor.MongoQueryMonitorDeviations
import kpn.database.actions.monitor.MongoQueryMonitorGroupRouteInfos
import kpn.database.actions.monitor.MongoQueryMonitorMemberCount
import kpn.database.actions.monitor.MongoQueryMonitorReferenceTileIds
import kpn.database.actions.monitor.MongoQueryMonitorReferenceTiles
import kpn.database.actions.monitor.MongoQueryMonitorRouteRelationInfos
import kpn.database.actions.monitor.MongoQueryMonitorStateDeviationInfos
import kpn.database.actions.monitor.MongoQueryMonitorStateTileIds
import kpn.database.actions.monitor.MongoQueryMonitorStateTiles
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.MongoProjections.objectIdToString
import kpn.database.base.NameRow
import kpn.database.base.ObjectIdId
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.domain.MonitorGroupRouteCount
import kpn.server.monitor.domain.MonitorGroupRouteInfo
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
import org.mongodb.scala.bson.BsonNull
import org.mongodb.scala.bson.conversions.Bson
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
    database.monitorRoutes.optionAggregate(pipeline, classOf[MonitorRoute], log)
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
    database.monitorReferences.optionAggregate(pipeline, classOf[MonitorReference], log)
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
    database.monitorReferences.optionAggregate(pipeline, classOf[ObjectIdId], log).map(_._id)
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
    database.monitorReferences.aggregate(pipeline, classOf[MonitorReference], log)
  }

  override def oldReferences(routeId: ObjectId): Seq[OldMonitorReference] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId.raw),
      )
    )
    database.oldMonitorReferences.aggregate(pipeline, classOf[OldMonitorReference], log)
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

    database.monitorReferences.aggregate(pipeline, classOf[MonitorReferenceId], log)
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

    database.monitorReferences.optionAggregate(pipeline, classOf[Distance], log).map(_.referenceDistance)
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
    database.monitorStates.optionAggregate(pipeline, classOf[MonitorState], log)
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
    database.monitorStateTiles.aggregate(pipeline, classOf[MonitorStateTile], log)
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
    database.monitorStates.aggregate(pipeline, classOf[MonitorState], log)
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

  override def deleteStateTile(tileId: ObjectId): Unit = {
    database.monitorStateTiles.deleteByObjectId(tileId)
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
    database.monitorStates.aggregate(pipeline, classOf[MonitorRouteCount], log).map(_.count).sum
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
    database.monitorStates.aggregate(pipeline, classOf[MonitorRouteCount], log).map(_.count).sum
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
          arraySize("deviationCount", "$deviations"),
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

    database.monitorStates.optionAggregate(pipeline, classOf[MonitorStateSummary], log)
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
          arraySize("deviationCount", "$deviations")
        )
      ),
    )

    database.monitorStates.aggregate(pipeline, classOf[MonitorStateSummary], log)
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

    database.monitorStates.aggregate(pipeline, classOf[MonitorStateId], log)
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
    ???
    //    database.monitorReferences.findOne(
    //      filter(
    //        equal("routeId", routeMonitorId),
    //      ),
    //      log
    //    )
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
      val changes = database.monitorRouteChanges.aggregate(pipeline, classOf[MonitorRouteChange], log)
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
    database.monitorRoutes.aggregate(pipeline, classOf[MonitorGroupRouteCount], log)
  }

  override def groupRouteInfos(): Seq[MonitorGroupRouteInfo] = {
    new MongoQueryMonitorGroupRouteInfos(database).execute()
  }

  override def groupRouteDetails(groupId: ObjectId): Seq[MonitorRouteDetail] = {
    val pipeline = Seq(
      filter(
        equal("groupId", groupId.raw)
      ),
      project(
        fields(
          excludeId(),
          objectIdToString("routeId", "$_id"),
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
    database.monitorRoutes.aggregate(pipeline, classOf[MonitorRouteDetail], log)
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
    database.monitorRouteChanges.aggregate(pipeline, classOf[MonitorRouteChange], log)
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
    database.monitorRouteChanges.aggregate(pipeline, classOf[MonitorRouteChange], log)
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
    database.monitorRoutes.aggregate(pipeline, classOf[NameRow], log).map(_.name)
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

  override def stateTilesBytTileId(tileId: TileId): Seq[MonitorStateTile] = {
    new MongoQueryMonitorStateTiles(database).execute(tileId)
  }

  override def referenceTiles(tileId: TileId): Seq[MonitorReferenceTileInfo] = {
    new MongoQueryMonitorReferenceTiles(database).execute(tileId)
  }

  override def routeDeviations(routeId: ObjectId): Seq[MonitorRouteDeviationInfo] = {
    new MongoQueryMonitorDeviations(database).execute(routeId)
  }

  override def routeMemberCount(relationId: Long): Long = {
    new MongoQueryMonitorMemberCount(database).execute(relationId)
  }

  override def stateDeviationInfos(routeId: ObjectId): Seq[MonitorStateDeviationInfo] = {
    new MongoQueryMonitorStateDeviationInfos(database).execute(routeId)
  }

  override def routeRelationInfos(relationIds: Seq[Long]): Seq[MonitorRouteRelationInfo] = {
    new MongoQueryMonitorRouteRelationInfos(database).execute(relationIds)
  }
}
