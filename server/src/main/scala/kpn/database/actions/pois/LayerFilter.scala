package kpn.database.actions.pois

import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson

object LayerFilter {

  def of(layers: Seq[String]): Option[Bson] = {
    Option.when(layers.nonEmpty) {
      val quotedLayers = layers.map(l => s"\"$l\"").mkString(",")
      val elemMatch = s"""{"layers": {"$$elemMatch": { "$$in": [$quotedLayers]}}}}"""
      BsonDocument(elemMatch)
    }
  }
}
