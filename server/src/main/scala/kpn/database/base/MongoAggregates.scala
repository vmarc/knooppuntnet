package kpn.database.base

import com.mongodb.client.model.Aggregates.`match`
import com.mongodb.client.model.Facet
import com.mongodb.client.model.Filters
import kpn.core.util.Util.seqToList
import kpn.database.base.Types.MongoPipeline
import org.bson.BsonDocument
import org.bson.conversions.Bson

object MongoAggregates {

  def filter(filter: Bson): Bson = {
    `match`(filter)
  }

  def equal(field: String, value: Any): Bson = {
    Filters.eq(field, value)
  }

  def notEqual(field: String, value: Any): Bson = {
    Filters.ne(field, value)
  }

  def ffacet(name: String, pipeline: MongoPipeline): Facet = {
    new Facet(name, seqToList(pipeline))
  }

  def unionWith(collection: String, pipeline: MongoPipeline): Bson = {
    com.mongodb.client.model.Aggregates.unionWith(collection, seqToList(pipeline))
  }

  def lookup(collection: String, pipeline: MongoPipeline, field: String): Bson = {
    com.mongodb.client.model.Aggregates.lookup(collection, seqToList(pipeline), field)
  }

  def arrayEmpty(field: String): Bson = {
    BsonDocument.parse(s"""{$field: { $$exists: true, $$size: 0}}""")
  }
}
