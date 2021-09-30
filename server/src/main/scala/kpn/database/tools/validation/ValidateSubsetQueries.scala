package kpn.database.tools.validation

import kpn.api.custom.Subset
import kpn.database.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.database.actions.subsets.MongoQuerySubsetOrphanRoutes
import kpn.database.base.Database

class ValidateSubsetQueries(database: Database) {

  def validate(): Seq[ValidationResult] = {
    Seq(
      validateSubsetOrphanNodes(),
      validateSubsetOrphanRoutes()
    )
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
