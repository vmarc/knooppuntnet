package kpn.database.index

import com.mongodb.client.model.IndexOptions
import kpn.core.util.Log
import kpn.database.base.Database

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
        collection.native.createIndex(index.index, new IndexOptions().name(index.indexName))
        ("Created", ())
      }
    }
  }

  def dropIndex(index: Index): Unit = {
    val collection = database.getCollection(index.collection.name)
    try {
      collection.native.dropIndex(index.index)
      log.info("dropped")
    }
    catch {
      case e: Exception =>
        log.warn(s"Could not drop index (does not exist, or index definition does not match)?)")
    }
  }
}
