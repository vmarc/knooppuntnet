package kpn.database.tools.validation

import kpn.database.base.Database

class ValidateRouteQueries(database: Database) {
  def validate(): Seq[ValidationResult] = {
    // MongoFindById
    // MongoQueryIds
    Seq.empty
  }
}
