package kpn.database.actions.nodes

import kpn.api.common.common.Reference
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryNodeBaseNetworkReferences {
  private val log = Log(classOf[MongoQueryNodeBaseNetworkReferences])
}

class MongoQueryNodeBaseNetworkReferences(database: Database) {

  def execute(nodeId: Long, log: Log = MongoQueryNodeBaseNetworkReferences.log): Seq[Reference] = {
    log.infoElapsed {
      val references = database.baseNetworks.aggregate[Reference](pipeline(nodeId), log)
      (s"node network references: ${references.size}", references)
    }
  }

  private def pipeline(nodeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("nodeIds", nodeId),
        )
      ),
      unwind("$members"),
      filter(
        and(
          equal("members.memberType", "node"),
          equal("members.ref", nodeId),
        )
      ),
      project(
        fields(
          excludeId(),
          include("routeType"),
          include("routeScope"),
          computed("id", "$_id"),
          include("name"),
          computed("role", "$members.role"),
        )
      )
    )
  }
}
