package kpn.core.mongo.tools.validation

import kpn.core.mongo.Database
import kpn.core.mongo.actions.base.MongoQueryIds

class ValidateNetworkQueries(database: Database) {

  def validate(): Seq[ValidationResult] = {
    Seq(
      validateQueryIds(),
      validateQueryFindById(),
      validateQueryNetworkChangeCount(),
      validateQueryNetworkChangeCounts(),
      validateQueryNetworkChanges()
    )
  }

  private def validateQueryIds(): ValidationResult = {
    ValidationResult.validate("MongoQueryIds/networks") {
      val networkIds = database.networks.ids()
      if (networkIds.size < 300) {
        Some(s"number of networks is less than expected 300 (actual: ${networkIds.size})")
      }
      else {
        None
      }
    }
  }

  private def validateQueryFindById(): ValidationResult = {
    ValidationResult.validate("MongoFindById/networks") {
      database.networks.findById(1066154L) match {
        case None => Some("network with id 1066154 not found")
        case Some(networkInfo) =>
          if (networkInfo.attributes.name != "Achterhoek") {
            Some("network with 1066154 does not have the expected name 'Achterhoek'")
          }
          else {
            None
          }
      }
    }
  }

  private def validateQueryNetworkChangeCount(): ValidationResult = {
    ValidationResult.validate("MongoQueryNetworkChangeCount") {
      Some("TODO")
    }
  }

  private def validateQueryNetworkChangeCounts(): ValidationResult = {
    ValidationResult.validate("MongoQueryNetworkChangeCounts") {
      Some("TODO")
    }
  }

  private def validateQueryNetworkChanges(): ValidationResult = {
    ValidationResult.validate("MongoQueryNetworkChanges") {
      Some("TODO")
    }
  }
}
