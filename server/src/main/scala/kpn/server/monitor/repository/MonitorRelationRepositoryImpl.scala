package kpn.server.monitor.repository

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.monitor.domain.MonitorRelation
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Accumulators.addToSet
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.include

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
    database.monitorRelations.aggregate[MonitorTileData](pipeline, log)
  }
}
