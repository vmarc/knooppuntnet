package kpn.database.actions.pois

import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson

object LayerFilter {

  def of(layers: Seq[String]): Option[Bson] = {
    if (layers.nonEmpty) {
      val quotedLayers = layers.map(l => "\"" + l + "\"").mkString(",")
      val elemMatch = s"""{"layers": {"$$elemMatch": { "$$in": [$quotedLayers]}}}}"""
      Some(
        BsonDocument(elemMatch),
      )
    }
    else {
      None
    }
  }
}
