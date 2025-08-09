package kpn.database.actions.changes

import kpn.core.util.Log
import kpn.core.util.Util.seqToList
import kpn.database.actions.changes.MongoQueryChangeSetRefs.log
import kpn.database.actions.changes.MongoQueryChangeSetRefs.pipeline
import kpn.database.actions.statistics.ChangeSetRef
import kpn.database.base.Database
import kpn.database.base.MongoQuery

import scala.jdk.CollectionConverters.IterableHasAsScala

object MongoQueryChangeSetRefs extends MongoQuery {
  private val log = Log(classOf[MongoQueryChangeSetRefs])
  private val pipeline = readPipeline("pipeline")
}

class MongoQueryChangeSetRefs(database: Database) {

  def execute(): Seq[ChangeSetRef] = {
    log.debugElapsed {
      val collection = database.getCollection("changeset-summaries")
      val values = collection.aggregate(seqToList(pipeline.stages), classOf[ChangeSetRef]).asScala.toSeq
      (s"${values.size} changeSetRefs", values)
    }
  }
}
