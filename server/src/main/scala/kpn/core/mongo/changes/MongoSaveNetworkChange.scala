package kpn.core.mongo.changes

import kpn.api.common.changes.details.NetworkChange
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
      val filter = equal("_id", networkChange._id)
      val collection = database.getCollection[NetworkChange]("network-changes")
      val future = collection.replaceOne(filter, networkChange, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save ${networkChange._id}", result)
    }
  }
}
