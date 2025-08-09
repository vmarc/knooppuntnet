package kpn.server.monitor.repository

import com.mongodb.client.model.Accumulators.addToSet
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.filter
import kpn.server.monitor.domain.MonitorRelation

class MonitorRelationRepositoryImpl(database: Database) extends MonitorRelationRepository {

  private val log = Log(classOf[MonitorRelationRepositoryImpl])

  override def save(monitorRelation: MonitorRelation): Unit = {
    database.monitorRelations.save(monitorRelation, log)
  }

  override def allTiles(): Seq[MonitorTileData] = {
    val pipeline = Seq(
      project(
        fields(
          include("tiles")
        )
      ),
      unwind("$tiles"),
      group(
        "$tiles",
        addToSet("relationIds", "$_id")
      ),
      project(
        fields(
          excludeId(),
          computed("name", "$_id"),
          include("relationIds")
        )
      ),
    )
    database.monitorRelations.aggregate(pipeline, classOf[MonitorTileData], log)
  }

  def tilesZoomLevel(zoomLevel: Long): Seq[MonitorTileData] = {

    val pipeline = Seq(
      project(
        fields(
          include("tiles")
        )
      ),
      unwind("$tiles"),
      filter(
        regex("tiles", s"^$zoomLevel-")
      ),
      group(
        "$tiles",
        addToSet("relationIds", "$_id")
      ),
      project(
        fields(
          excludeId(),
          computed("name", "$_id"),
          include("relationIds")
        )
      ),
    )
    database.monitorRelations.aggregate(pipeline, classOf[MonitorTileData], log)
  }
}
