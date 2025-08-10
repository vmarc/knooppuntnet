package kpn.database.base

import org.bson.BsonDocument
import org.bson.conversions.Bson

object MongoProjections {
  def concat(fieldName: String, elements: String*): Bson = {
    val elementString = elements.map(element => s"\"$element\"").mkString("[", ",", "]")
    BsonDocument.parse(s"""{"$fieldName": {"$$concat": $elementString}}""")
  }

  def objectIdToString(fieldName: String): Bson = {
    BsonDocument.parse(s"""{"$fieldName": {"$$toString": "$$$fieldName"}}""")
  }

  def objectIdToString(fieldName: String, valueFieldName: String): Bson = {
    BsonDocument.parse(s"""{"$fieldName": {"$$toString": "$valueFieldName"}}""")
  }

  def arraySize(fieldName: String, valueFieldName: String): Bson = {
    BsonDocument.parse(s"""{"$fieldName": {"$$size": "$valueFieldName"}}""")
  }
}
