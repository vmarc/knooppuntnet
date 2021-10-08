package kpn.database.actions.subsets

import kpn.api.common.subset.SubsetMapNetwork
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

object MongoQuerySubsetMapNetworks {
  private val log = Log(classOf[MongoQuerySubsetInfo])
}

class MongoQuerySubsetMapNetworks(database: Database) {

  def execute(subset: Subset, log: Log = MongoQuerySubsetMapNetworks.log): Seq[SubsetMapNetwork] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("country", subset.country.domain),
            equal("summary.networkType", subset.networkType.name),
          )
        ),
        project(
          fields(
            computed("id", "$_id"),
            computed("name", "$summary.name"),
            computed("km", "$detail.km"),
            computed("nodeCount", "$summary.nodeCount"),
            computed("routeCount", "$summary.routeCount"),
            computed("center", "$detail.center"),
          )
        )
      )

      val subsetMapNetworks = database.networkInfos.aggregate[SubsetMapNetwork](pipeline)
      (s"subset ${subset.name} ${subsetMapNetworks.size} networks", subsetMapNetworks)
    }
  }
}
