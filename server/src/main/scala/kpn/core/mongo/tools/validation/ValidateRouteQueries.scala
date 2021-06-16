package kpn.core.mongo.tools.validation

import kpn.core.mongo.Database

class ValidateRouteQueries(database: Database) {
  def validate(): Seq[ValidationResult] = {
    // MongoFindById
    // MongoQueryIds
    Seq.empty
  }
}
