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
import kpn.server.repository.NetworkRepository
import org.bson.BsonNull
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web", "analysis"))
class MonitorRouteRepository(database: Database) {

  private val log = Log(classOf[NetworkRepository])

  // *** MonitorRoute ***

  def allRouteIds: Seq[Long] = {
    database.monitorRoutes.ids(log)
  }

  def routeById(monitorRouteId: ObjectId): Option[MonitorRoute] = {
    database.monitorRoutes.findByObjectId(monitorRouteId, log)
  }

  def routeByName(groupId: ObjectId, routeName: String): Option[MonitorRoute] = {
    val pipeline = Seq(
      filter(
        and(
          equal("groupId", groupId),
          equal("name", routeName),
        )
      )
    )
    database.monitorRoutes.optionAggregate(pipeline, classOf[MonitorRoute], log)
  }

  def saveRoute(route: MonitorRoute): Unit = {
    database.monitorRoutes.save(route, log)
  }

  def deleteRoute(routeId: ObjectId): Unit = {
    new MonitorRouteDelete(database).delete(routeId, log)
  }

  // *** MonitorReference ***

  def saveReference(reference: MonitorReference): Unit = {
    database.monitorReferences.save(reference, log)
  }

  def deleteReferences(routeId: ObjectId): Unit = {
    val routeFilter = equal("routeId", routeId)
    database.monitorReferences.deleteMany(routeFilter, log)
  }

  def deleteReference(routeId: ObjectId, subRelationId: Long): Unit = {
    val routeReferenceFilter = and(
      equal("routeId", routeId),
      equal("relationId", subRelationId)
    )
    database.monitorReferences.deleteMany(routeReferenceFilter, log)
  }

  def deleteReferenceById(objectId: ObjectId): Unit = {
    database.monitorReferences.deleteByObjectId(objectId, log)
  }

  def reference(routeId: ObjectId, relationId: Option[Long]): Option[MonitorReference] = {
    val relationIdValue = relationId match {
      case Some(value) => value
      case None => new BsonNull()
    }
    val pipeline = Seq(
      routeReferenceFilter(routeId, relationId)
    )
    database.monitorReferences.optionAggregate(pipeline, classOf[MonitorReference], log)
  }

  def routeRelationReferenceId(routeId: ObjectId, relationId: Option[Long]): Option[ObjectId] = {
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
      case None => new BsonNull()
    }
    filter(
      and(
        equal("routeId", routeId),
        equal("relationId", relationIdValue),
      )
    )
  }

  def references(routeId: ObjectId): Seq[MonitorReference] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
      )
    )
    database.monitorReferences.aggregate(pipeline, classOf[MonitorReference], log)
  }

  def oldReferences(routeId: ObjectId): Seq[OldMonitorReference] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
      )
    )
    database.oldMonitorReferences.aggregate(pipeline, classOf[OldMonitorReference], log)
  }

  def referenceIds(routeId: ObjectId): Seq[MonitorReferenceId] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
      ),
      project(
        fields(
          include("relationId"),
        )
      ),
    )

    database.monitorReferences.aggregate(pipeline, classOf[MonitorReferenceId], log)
  }

  def superRouteReferenceSummary(routeId: ObjectId): Option[Long] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
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

  def state(routeId: ObjectId, relationId: Long): Option[MonitorState] = {
    val pipeline = Seq(
      filter(
        and(
          equal("routeId", routeId),
          equal("relationId", relationId),
        ),
      ),
    )
    database.monitorStates.optionAggregate(pipeline, classOf[MonitorState], log)
  }

  def stateTiles(routeId: ObjectId, relationId: Long): Seq[MonitorStateTile] = {
    val pipeline = Seq(
      filter(
        and(
          equal("routeId", routeId),
          equal("relationId", relationId),
        ),
      ),
    )
    database.monitorStateTiles.aggregate(pipeline, classOf[MonitorStateTile], log)
  }

  def states(routeId: ObjectId): Seq[MonitorState] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
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

  def saveState(state: MonitorState): Unit = {
    database.monitorStates.save(state, log)
  }

  def saveStateTile(stateTile: MonitorStateTile): Unit = {
    database.monitorStateTiles.save(stateTile, log)
  }

  def deleteStates(routeId: ObjectId): Unit = {
    val routeFilter = equal("routeId", routeId)
    database.monitorStates.deleteMany(routeFilter, log)
  }

  def deleteStateTiles(routeId: ObjectId): Unit = {
    val routeFilter = equal("routeId", routeId)
    database.monitorStateTiles.deleteMany(routeFilter, log)
  }

  def deleteStateTile(tileId: ObjectId): Unit = {
    database.monitorStateTiles.deleteByObjectId(tileId)
  }

  def deleteState(routeId: ObjectId, subRelationId: Long): Unit = {
    val routeReferenceFilter = and(
      equal("routeId", routeId),
      equal("relationId", subRelationId)
    )
    database.monitorStates.deleteMany(routeReferenceFilter, log)
  }

  def deleteStateById(objectId: ObjectId): Unit = {
    database.monitorStates.deleteByObjectId(objectId, log)
  }

  def stateCount(routeId: ObjectId): Long = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
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

  def stateSize(routeId: ObjectId): Long = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
      ),
      project(
        fields(
          include("routeId"),
          computed("size", Document.parse("""{ $sum: { $bsonSize: "$$ROOT" } }"""))
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

  def superRouteStateSummary(routeId: ObjectId): Option[MonitorStateSummary] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
      ),
      project(
        fields(
          excludeId(),
          computed("deviationDistance", Document.parse("""{ $sum: "$deviations.meters" }""")),
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

  def stateSummaries(routeId: ObjectId): Seq[MonitorStateSummary] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
      ),
      project(
        fields(
          excludeId(),
          include("relationId"),
          computed("deviationDistance", Document.parse("""{ $sum: "$deviations.meters" }""")),
          arraySize("deviationCount", "$deviations")
        )
      ),
    )

    database.monitorStates.aggregate(pipeline, classOf[MonitorStateSummary], log)
  }

  def stateIds(routeId: ObjectId): Seq[MonitorStateId] = {
    val pipeline = Seq(
      filter(
        equal("routeId", routeId),
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

  def saveRouteChange(routeChange: MonitorRouteChange): Unit = {
    database.monitorRouteChanges.save(routeChange, log)
  }

  def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit = {
    database.monitorRouteChangeGeometries.save(routeChangeGeometry, log)
  }

  def routeChange(changeKey: ChangeKey): Option[MonitorRouteChange] = {
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

  def routeChangeGeometry(changeKey: ChangeKey): Option[MonitorRouteChangeGeometry] = {
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

  def routeReferenceKey(routeMonitorId: String): Option[String] = {
    // TODO MONGO should be looking for most recent entry here, instead of assuming there is always exactly 1 entry ???
    ???
    //    database.monitorReferences.findOne(
    //      filter(
    //        equal("routeId", routeMonitorId),
    //      ),
    //      log
    //    )
  }

  def routeChange(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange] = {
    val _id = s"$monitorRouteId:$changeSetId:$replicationNumber"
    database.monitorRouteChanges.findByStringId(_id, log)
  }

  def routeChangeGeometry(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry] = {
    val _id = s"$monitorRouteId:$changeSetId:$replicationNumber"
    database.monitorRouteChangeGeometries.findByStringId(_id, log)
  }

  def changesCount(parameters: MonitorChangesParameters): Long = {
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

  def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
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

  def groupRouteCounts(): Seq[MonitorGroupRouteCount] = {
    val pipeline = Seq(
      group(
        new Document(
          java.util.Map.of(
            "groupId", "$groupId"
          )
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

  def groupRouteInfos(): Seq[MonitorGroupRouteInfo] = {
    new MongoQueryMonitorGroupRouteInfos(database).execute()
  }

  def groupRouteDetails(groupId: ObjectId): Seq[MonitorRouteDetail] = {
    val pipeline = Seq(
      filter(
        equal("groupId", groupId)
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

  def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long = {
    val changesFilter = groupChangesFilter(groupName, parameters)
    database.monitorRouteChanges.countFilteredDocuments(changesFilter, log)
  }

  def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
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

  def routeChangesCount(id: String, parameters: MonitorChangesParameters): Long = {
    val changesFilter = routeChangesCountFilter(id, parameters)
    database.monitorRouteChanges.countFilteredDocuments(changesFilter, log)
  }

  def routeChanges(monitorRouteId: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
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

  def routeNames(groupId: ObjectId): Seq[String] = {
    val pipeline = Seq(
      filter(
        equal("groupId", groupId)
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

  def routes(): Seq[MonitorRoute] = {
    database.monitorRoutes.findAll(log)
  }

  def stateTileIds(): Seq[TileId] = {
    new MongoQueryMonitorStateTileIds(database).execute()
  }

  def referenceTileIds(): Seq[TileId] = {
    new MongoQueryMonitorReferenceTileIds(database).execute()
  }

  def stateTilesBytTileId(tileId: TileId): Seq[MonitorStateTile] = {
    new MongoQueryMonitorStateTiles(database).execute(tileId)
  }

  def referenceTiles(tileId: TileId): Seq[MonitorReferenceTileInfo] = {
    new MongoQueryMonitorReferenceTiles(database).execute(tileId)
  }

  def routeDeviations(routeId: ObjectId): Seq[MonitorRouteDeviationInfo] = {
    new MongoQueryMonitorDeviations(database).execute(routeId)
  }

  def routeMemberCount(relationId: Long): Long = {
    new MongoQueryMonitorMemberCount(database).execute(relationId)
  }

  def stateDeviationInfos(routeId: ObjectId): Seq[MonitorStateDeviationInfo] = {
    new MongoQueryMonitorStateDeviationInfos(database).execute(routeId)
  }

  def routeRelationInfos(relationIds: Seq[Long]): Seq[MonitorRouteRelationInfo] = {
    new MongoQueryMonitorRouteRelationInfos(database).execute(relationIds)
  }
}
