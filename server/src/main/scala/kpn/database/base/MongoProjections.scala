package kpn.database.base

import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson

object MongoProjections {
  def concat(fieldName: String, elements: String*): Bson = {
    val elementString = elements.map(element => s"\"$element\"").mkString("[", ",", "]")
    BsonDocument(s"""{"$fieldName": {"$$concat": $elementString}}""")
  }
}
