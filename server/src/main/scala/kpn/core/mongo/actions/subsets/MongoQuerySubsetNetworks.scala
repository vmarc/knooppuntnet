package kpn.core.mongo.actions.subsets

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetNetworks.log
import kpn.core.mongo.actions.subsets.MongoQuerySubsetNetworks.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object MongoQuerySubsetNetworks extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetNetworks])
  private val pipeline = readPipelineString("pipeline")
}

class MongoQuerySubsetNetworks(database: Database) {

  def execute(subset: Subset): Seq[NetworkAttributes] = {
    val args = Map(
      "@country" -> subset.country.domain,
      "@networkType" -> subset.networkType.name
    )
    log.debugElapsed {
      val networks = database.networks.stringPipelineAggregate[NetworkInfo](pipeline, args, log)
      val result = s"subset ${subset.name} networks: ${networks.size}"
      (result, networks.map(_.attributes))
    }
  }
}
