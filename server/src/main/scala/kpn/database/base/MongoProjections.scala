package kpn.database.base

import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson

object MongoProjections {
  def concat(fieldName: String, elements: String*): Bson = {
    val elementString = elements.map(element => s"\"$element\"").mkString("[", ",", "]")
    BsonDocument(s"""{"$fieldName": {"$$concat": $elementString}}""")
  }

  def objectIdToString(fieldName: String): Bson = {
    BsonDocument(s"""{"$fieldName": {"$$toString": "$$$fieldName"}}""")
  }

  def objectIdToString(fieldName: String, valueFieldName: String): Bson = {
    BsonDocument(s"""{"$fieldName": {"$$toString": "$valueFieldName"}}""")
  }

  def arraySize(fieldName: String, valueFieldName: String): Bson = {
    BsonDocument(s"""{"$fieldName": {"$$size": "$valueFieldName"}}""")
  }
}
