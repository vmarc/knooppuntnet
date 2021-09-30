package kpn.database.tools.validation

import kpn.api.common.common.Reference
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.database.actions.nodes.MongoQueryNodeNetworkReferences
import kpn.database.base.Database

class ValidateNodeQueries(database: Database) {
  def validate(): Seq[ValidationResult] = {
    Seq(
      validateNetworkReferences()
    )
  }

  private def validateNetworkReferences(): ValidationResult = {
    ValidationResult.validate("MongoQueryNodeNetworkReferences") {
      val query = new MongoQueryNodeNetworkReferences(database)
      val references = query.execute(247742900L)
      val expectedReferences = Seq(
        Reference(NetworkType.cycling, NetworkScope.regional, 1066154, "Achterhoek"),
        Reference(NetworkType.cycling, NetworkScope.regional, 172106, "Veluwe")
      )
      if (!references.equals(expectedReferences)) {
        Some(s"Unexpected references: $references")
      }
      else {
        None
      }
    }
  }
}
