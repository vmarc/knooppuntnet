package kpn.core.mongo.actions.subsets

import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanRoutes.log
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanRoutes.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object MongoQuerySubsetOrphanRoutes extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetOrphanRoutes])
  private val pipeline = readPipelineString("pipeline")
}

class MongoQuerySubsetOrphanRoutes(database: Database) {

  def execute(subset: Subset): Seq[OrphanRouteInfo] = {
    val args = Map(
      "@country" -> subset.country.domain,
      "@networkType" -> subset.networkType.name
    )
    log.debugElapsed {
      val routes = database.routes.stringPipelineAggregate[OrphanRouteInfo](pipeline, args, log)
      val result = s"subset ${subset.name} orphan routes: ${routes.size}"
      (result, routes)
    }
  }
}
