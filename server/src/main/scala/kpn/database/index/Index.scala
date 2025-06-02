package kpn.database.index

import kpn.database.base.DatabaseCollection
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Indexes

object Index {
  def apply(
    collection: DatabaseCollection[?],
    indexName: String,
    fieldNames: String*
  ): Index = {
    val index = Indexes.ascending(fieldNames: _*)
    Index(
      collection: DatabaseCollection[?],
      indexName: String,
      index
    )
  }
}

case class Index(
  collection: DatabaseCollection[?],
  indexName: String,
  index: Bson
)
