package kpn.core.mongo.tools.validation

import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetNetworks
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanRoutes

class ValidateSubsetQueries(database: Database) {

  def validate(): Seq[ValidationResult] = {
    Seq(
      validateSubsetNetworks(),
      validateSubsetOrphanNodes(),
      validateSubsetOrphanRoutes()
    )
  }

  private def validateSubsetNetworks(): ValidationResult = {
    ValidationResult.validate("MongoQuerySubsetOrphanNodes") {
      val networks = new MongoQuerySubsetNetworks(database).execute(Subset.nlHiking)
      if (networks.size < 100) {
        Some(s"less than 100 bicycle networks in The Netherlands (${networks.size})")
      }
      else {
        None
      }
    }
  }

  private def validateSubsetOrphanNodes(): ValidationResult = {
    ValidationResult.validate("MongoQuerySubsetOrphanNodes") {
      val nodeInfos = new MongoQuerySubsetOrphanNodes(database).execute(Subset.deBicycle)
      if (nodeInfos.size < 10) {
        Some(s"less than 10 bicycle orphan nodes in Germany (${nodeInfos.size})")
      }
      else {
        None
      }
    }
  }

  private def validateSubsetOrphanRoutes(): ValidationResult = {
    ValidationResult.validate("MongoQuerySubsetOrphanRoutes") {
      val orphanRouteInfos = new MongoQuerySubsetOrphanRoutes(database).execute(Subset.deBicycle)
      if (orphanRouteInfos.size < 100) {
        Some(s"less than 100 bicycle orphan routes in Germany (${orphanRouteInfos.size})")
      }
      else {
        None
      }
    }
  }
}
