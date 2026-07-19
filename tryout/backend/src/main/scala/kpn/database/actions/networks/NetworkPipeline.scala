package kpn.database.actions.networks

import com.mongodb.client.model.Aggregates.count
import com.mongodb.client.model.Projections.computed
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoAggregates.lookup
import kpn.database.base.Types.MongoPipeline
import org.bson.Document
import org.bson.conversions.Bson

object NetworkPipeline {

  def lookupChangeCount(database: Database, networkId: Long): Bson = {
    lookup(database.networkChanges.name, pipelineChangeCount(networkId), "changeCountLookup")
  }

  def fields(extraProjections: Bson*): Bson = {
    val projections = extraProjections ++ summaryProjections
    com.mongodb.client.model.Projections.fields(
      projections: _*
    )
  }

  private val summaryProjections: MongoPipeline = Seq(
    computed("summary.name", "$base.name"),
    computed("summary.routeType", "$base.routeType"),
    computed("summary.routeScope", "$base.routeScope"),
    computed("summary.factCount", "$factCount"),
    computed("summary.nodeCount", "$nodeCount"),
    computed("summary.routeCount", "$routeCount"),
    computed("summary.changeCount", Document.parse("""{ "$first": "$changeCountLookup.count" }""")),
  )

  private def pipelineChangeCount(networkId: Long): MongoPipeline = {
    Seq(
      filter(
        equal("networkId", networkId)
      ),
      count("count")
    )
  }
}
