package kpn.database.actions.nodes

import kpn.api.common.common.Reference
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryNodeNetworkReferences {
  private val log = Log(classOf[MongoQueryNodeNetworkReferences])
}

class MongoQueryNodeNetworkReferences(database: Database) {

  def execute(nodeId: Long, log: Log = MongoQueryNodeNetworkReferences.log): Seq[Reference] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("active", true),
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
      val references = database.baseNetworks.aggregate[Reference](pipeline, log)
      (s"node network references: ${references.size}", references)
    }
  }
}
