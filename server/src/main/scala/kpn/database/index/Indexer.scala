package kpn.database.index

import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.ObservableFuture
import org.mongodb.scala.model.IndexOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Indexer(database: Database) {
  private val log = Log(classOf[Indexer])

  def createIndexes(): Unit = {
    val indexes = new IndexConfiguration(database).indexes
    log.info(s"Create ${indexes.size} indexes")
    indexes.foreach(createIndex)
    log.info("Done")
  }

  def createIndex(index: Index): Unit = {
    Log.context(s"collection: '${index.collection.name}', index: '${index.indexName}'") {
      dropIndex(index)
      log.infoElapsed {
        val collection = database.getCollection(index.collection.name)
        val future = collection.createIndex(index.index, IndexOptions().name(index.indexName)).toFuture()
        Await.result(future, Duration(25, TimeUnit.MINUTES))
        ("Created", ())
      }
    }
  }

  def dropIndex(index: Index): Unit = {
    val collection = database.getCollection(index.collection.name)
    try {
      val future = collection.dropIndex(index.index).toFuture()
      Await.result(future, Duration(25, TimeUnit.MINUTES))
      log.info("dropped")
    }
    catch {
      case e: Exception =>
        log.warn(s"Could not drop index (does not exist, or index definition does not match)?)")
    }
  }
}
