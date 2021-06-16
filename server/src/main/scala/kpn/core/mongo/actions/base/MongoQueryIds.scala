package kpn.core.mongo.actions.base

import kpn.core.mongo.util.MongoQuery
import kpn.core.mongo.util.Pipeline

object MongoQueryIds extends MongoQuery {
  val pipeline: Pipeline = readPipeline("pipeline")
}
