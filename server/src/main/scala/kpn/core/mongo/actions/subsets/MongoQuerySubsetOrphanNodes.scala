package kpn.core.mongo.actions.subsets

import kpn.api.common.NodeInfo
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanNodes.log
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanNodes.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object MongoQuerySubsetOrphanNodes extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetOrphanNodes])
  private val pipeline = readPipelineString("pipeline")
}

class MongoQuerySubsetOrphanNodes(database: Database) extends MongoQuery {

  def execute(subset: Subset): Seq[NodeInfo] = {
    val args = Map(
      "@country" -> subset.country.domain,
      "@networkType" -> subset.networkType.name
    )
    log.debugElapsed {
      val nodes = database.nodes.stringPipelineAggregate[NodeInfo](pipeline, args, log)
      val result = s"subset ${subset.name} orphan nodes: ${nodes.size}"
      (result, nodes)
    }
  }
}
