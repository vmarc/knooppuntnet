package kpn.core.mongo.tools

import kpn.core.mongo.Database
import kpn.core.mongo.tools.validation.ValidateNetworkQueries
import kpn.core.mongo.tools.validation.ValidateNodeQueries
import kpn.core.mongo.tools.validation.ValidatePoisQueries
import kpn.core.mongo.tools.validation.ValidateRouteQueries
import kpn.core.mongo.tools.validation.ValidateSubsetQueries
import kpn.core.mongo.tools.validation.ValidationResult
import kpn.core.mongo.util.Mongo

object ValidateQueriesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new ValidateQueriesTool(database).validate()
    }
  }
}

class ValidateQueriesTool(database: Database) {

  def validate(): Unit = {
    val validationResults = Seq(
      validateNetworkQueries(),
      validateRouteQueries(),
      validateNodeQueries(),
      validateChangesQueries(),
      validateSubsetQueries(),
      validateStatisticsQueries(),
      validatePoisQueries()
    ).flatten

    validationResults.foreach(println)
    println()

    if (validationResults.forall(validationResult => validationResult.isOk)) {
      println("All ok")
    }
    else {
      println("Validation error(s) found:")
      validationResults.filterNot(_.isOk).foreach { validationResult =>
        val result = validationResult.message match {
          case Some(message) => message
          case None =>
            if (validationResult.elapsedMillis > validationResult.expectedMillis) {
              s"query takes too long (elapsed: ${validationResult.elapsedMillis}ms, expected: ${validationResult.expectedMillis}ms)"
            }
            else {
              "?"
            }
        }
        println(s"  - ${validationResult.name}: $result")
      }
    }
  }

  private def validateNetworkQueries(): Seq[ValidationResult] = {
    new ValidateNetworkQueries(database).validate()
  }

  private def validateRouteQueries(): Seq[ValidationResult] = {
    new ValidateRouteQueries(database).validate()
  }

  private def validateNodeQueries(): Seq[ValidationResult] = {
    new ValidateNodeQueries(database).validate()
  }

  private def validateChangesQueries(): Seq[ValidationResult] = {
    // MongoFindById
    // MongoQueryIds
    Seq.empty
  }

  private def validateSubsetQueries(): Seq[ValidationResult] = {
    new ValidateSubsetQueries(database).validate()
  }

  private def validateStatisticsQueries(): Seq[ValidationResult] = {
    Seq.empty
  }

  private def validatePoisQueries(): Seq[ValidationResult] = {
    new ValidatePoisQueries(database).validate()
  }
}
