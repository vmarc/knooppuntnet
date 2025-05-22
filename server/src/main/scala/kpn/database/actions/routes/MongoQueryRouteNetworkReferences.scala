package kpn.database.actions.routes

import kpn.api.common.common.Reference
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteNetworkReferences {
  private val log = Log(classOf[MongoQueryRouteNetworkReferences])
}

class MongoQueryRouteNetworkReferences(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteNetworkReferences.log): Seq[Reference] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeId)
      val references = database.baseNetworks.aggregate[Reference](pipeline, log)
      (s"route network references: ${references.size}", references)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("relationIds", routeId),
        )
      ),
      unwind("$members"),
      filter(
        and(
          equal("members.memberType", "relation"),
          equal("members.ref", routeId),
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

  private def buildIdPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(equal("active", true)),
      unwind("$relationMembers"),
      filter(in("relationMembers.relationId", routeIds: _*)),
      project(
        fields(
          include("_id")
        )
      )
    )
  }
}
