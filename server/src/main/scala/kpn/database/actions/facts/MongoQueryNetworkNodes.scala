package kpn.database.actions.facts

import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQueryNetworkNodes.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.repository.NetworkElement
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

object MongoQueryNetworkNodes {
  private val log = Log(classOf[MongoQueryNetworkNodes])
}

class MongoQueryNetworkNodes(database: Database) {
  def execute(subset: Subset, nodeIds: Seq[Long]): Seq[NetworkElement] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, nodeIds)
      val references = database.networks.aggregate[NetworkElement](pipeline, log)
      (s"node network references: ${references.size}", references)
    }
  }

  private def buildPipeline(subset: Subset, nodeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.entryName),
          equal("summary.routeType", subset.routeType.entryName)
        )
      ),
      unwind("$nodes"),
      filter(in("nodes.id", nodeIds: _*)),
      project(
        fields(
          excludeId(),
          computed("networkId", "$_id"),
          computed("networkName", "$summary.name"),
          computed("elementId", "$nodes.id"),
        )
      )
    )
  }
}
