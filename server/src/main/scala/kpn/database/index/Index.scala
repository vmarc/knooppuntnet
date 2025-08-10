package kpn.database.index

import com.mongodb.client.model.Indexes
import kpn.database.base.DatabaseCollection
import org.bson.conversions.Bson

object Index {
  def apply(
    collection: DatabaseCollection[?],
    indexName: String,
    fieldNames: String*
  ): Index = {
    val index = Indexes.ascending(fieldNames *)
    new Index(
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
