package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.common.Reference
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryNodeBaseNetworkReferences {
  private val log = Log(classOf[MongoQueryNodeBaseNetworkReferences])
}

class MongoQueryNodeBaseNetworkReferences(database: Database) {

  def execute(nodeId: Long, log: Log = MongoQueryNodeBaseNetworkReferences.log): Seq[Reference] = {
    log.infoElapsed {
      val references = database.baseNetworks.aggregate(pipeline(nodeId), classOf[Reference], log)
      val updatedReferences = references.map { ref =>
        if (ref.name == null) {
          ref.copy(name = "") // needed because BaseNetworkDoc.name is Option[String]
        }
        else {
          ref
        }
      }
      (s"node network references: ${updatedReferences.size}", updatedReferences)
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
