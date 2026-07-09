package kpn.server.repository

import kpn.api.common.Fact
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_IntegrityCheckFailed
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_NetworkFactsWithElementIds
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_NetworkFactsWithElementRefs
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_Route
import kpn.database.base.Database
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class FactRefRepository(database: Database) {

  private val log = Log(classOf[FactRepository])

  def factRefs(subset: Subset, fact: Fact): SubsetFactRefs = {
    fact match {
      case Fact.NetworkExtraMemberNode => networkFactsWithElementIdsFactRefs(subset, fact, "node")
      case Fact.NetworkExtraMemberWay => networkFactsWithElementIdsFactRefs(subset, fact, "way")
      case Fact.NetworkExtraMemberRelation => networkFactsWithElementIdsFactRefs(subset, fact, "relation")
      case Fact.NodeMemberMissing => networkFactsWithElementRefsFactRefs(subset, fact, "node")
      case Fact.IntegrityCheckFailed => integrityCheckFailedFactRefs(subset)
      case _ => routeFactRefs(subset, fact)
    }
  }

  private def routeFactRefs(subset: Subset, fact: Fact): SubsetFactRefs = {
    new MongoQuerySubsetFactRefs_Route(database).execute(subset, fact)
  }

  private def networkFactsWithElementIdsFactRefs(subset: Subset, fact: Fact, elementType: String): SubsetFactRefs = {
    new MongoQuerySubsetFactRefs_NetworkFactsWithElementIds(database).execute(subset, fact, elementType)
  }

  private def networkFactsWithElementRefsFactRefs(subset: Subset, fact: Fact, elementType: String): SubsetFactRefs = {
    new MongoQuerySubsetFactRefs_NetworkFactsWithElementRefs(database).execute(subset, fact, elementType)
  }

  private def integrityCheckFailedFactRefs(subset: Subset): SubsetFactRefs = {
    new MongoQuerySubsetFactRefs_IntegrityCheckFailed(database).execute(subset)
  }
}
