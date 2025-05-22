package kpn.database.actions.nodes

import kpn.api.common.common.Reference
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

object MongoQueryNodeNetworkReferences {
  private val log = Log(classOf[MongoQueryNodeBaseNetworkReferences])
}

class MongoQueryNodeNetworkReferences(database: Database) {

  def execute(nodeId: Long, log: Log = MongoQueryNodeNetworkReferences.log): Seq[Reference] = {
    log.debugElapsed {
      val references = database.networks.aggregate[Reference](pipeline(nodeId), log)
      (s"node network references: ${references.size}", references)
    }
  }

  private def pipeline(nodeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("nodes.id", nodeId),
        )
      ),
      project(
        fields(
          excludeId(),
          computed("routeType", "$summary.routeType"),
          computed("routeScope", "$summary.routeScope"),
          computed("id", "$_id"),
          computed("name", "$summary.name"),
        )
      )
    )
  }
}
