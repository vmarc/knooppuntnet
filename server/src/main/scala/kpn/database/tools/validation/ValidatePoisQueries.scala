package kpn.database.tools.validation

import kpn.database.actions.pois.MongoQueryPoiAllTiles
import kpn.database.actions.pois.MongoQueryPoiElementIds
import kpn.database.actions.pois.MongoQueryTilePois
import kpn.database.base.Database

class ValidatePoisQueries(database: Database) {

  def validate(): Seq[ValidationResult] = {
    Seq(
      validateQueryAllTiles(),
      validateQueryPoiElementIds(),
      validateQueryTilePois()
    )
  }

  private def validateQueryAllTiles(): ValidationResult = {
    ValidationResult.validateMillis("MongoQueryPoiAllTiles", 15000) {
      val tiles = new MongoQueryPoiAllTiles(database).execute()
      val min = 280000
      if (tiles.size < min) {
        Some(s"number of tiles is less than expected $min (actual: ${tiles.size})")
      }
      else {
        None
      }
    }
  }

  private def validateQueryPoiElementIds(): ValidationResult = {
    ValidationResult.validateMillis("MongoQueryPoiElementIds", 15000) {
      val ids = new MongoQueryPoiElementIds(database).execute("node")
      val min = 1200000
      if (ids.size < min) {
        Some(s"number of pois with type node is less than expected $min (actual: ${ids.size})")
      }
      else {
        None
      }
    }
  }

  private def validateQueryTilePois(): ValidationResult = {
    ValidationResult.validate("MongoQueryTilePois") {
      val poiInfos = new MongoQueryTilePois(database).execute("14-8796-5374")
      val min = 500
      if (poiInfos.size < min) {
        Some(s"number of pois in tile is less than expected $min (actual: ${poiInfos.size})")
      }
      else {
        None
      }
    }
  }
}
