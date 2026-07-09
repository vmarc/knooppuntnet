package kpn.database.actions.pois

import org.bson.BsonDocument
import org.bson.conversions.Bson

object LayerFilter {

  def of(layers: Seq[String]): Option[Bson] = {
    Option.when(layers.nonEmpty) {
      val quotedLayers = layers.map(l => s"\"$l\"").mkString(",")
      val elemMatch = s"""{"layers": {"$$elemMatch": { "$$in": [$quotedLayers]}}}}"""
      BsonDocument.parse(elemMatch)
    }
  }
}
