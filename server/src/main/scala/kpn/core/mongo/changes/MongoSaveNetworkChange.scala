package kpn.core.mongo.changes

import kpn.api.common.changes.details.NetworkChange
import kpn.core.database.doc.NetworkChangeDoc
import kpn.core.mongo.changes.MongoSaveNetworkChange.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveNetworkChange {
  private val log = Log(classOf[MongoSaveNetworkChange])
}

class MongoSaveNetworkChange(database: MongoDatabase) {

  def execute(networkChange: NetworkChange): Unit = {
    log.debugElapsed {
      val networkId = networkChange.networkId
      val id = s"change:${networkChange.key.changeSetId}:${networkChange.key.replicationNumber}:network:$networkId"
      val doc = NetworkChangeDoc(id, networkChange)
      val filter = equal("_id", id)
      val collection = database.getCollection[NetworkChangeDoc]("network-changes")
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save $id", result)
    }
  }
}
