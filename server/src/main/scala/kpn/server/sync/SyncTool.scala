package kpn.server.sync

import kpn.api.base.ObjectId
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.sync.SyncTool.log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.include

object SyncTool {
  private val log = Log(classOf[SyncTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { sourceDatabase =>
      Mongo.executeIn("kpn-laptop") { targetDatabase =>
        val tool = new SyncTool(sourceDatabase, targetDatabase)
        //tool.generateInitialTransactions()
        tool.processTransactions()
        //tool.syncRoutes()
      }
    }
  }
}

class SyncTool(sourceDatabase: Database, targetDatabase: Database) {

  def generateInitialTransactions(): Unit = {
    sourceDatabase.routes.ids().foreach { routeId =>
      sourceDatabase.transactions.save(Transaction.routeUpdate(routeId))
    }
  }

  def processTransactions(): Unit = {
    sourceDatabase.transactions.findAll().foreach { transaction =>
      if (transaction.action == "update") {
        processUpdateTransaction(transaction)
      }
      else if (transaction.action == "delete") {
        processDeleteTransaction(transaction)
      }
    }
  }

  def syncRoutes(): Unit = {
    val sourceMap = stamps("source", sourceDatabase)
    val targetMap = stamps("target", targetDatabase)
    delete(sourceMap, targetMap)
    update(sourceMap, targetMap)
  }

  private def processDeleteTransaction(transaction: Transaction): Unit = {
    targetDatabase.routes.delete(transaction.id)
    sourceDatabase.transactions.deleteByObjectId(transaction._id)
  }

  private def processUpdateTransaction(transaction: Transaction): Unit = {
    sourceDatabase.routes.findById(transaction.id) match {
      case None =>
      case Some(route) =>
        targetDatabase.routes.save(route)
        sourceDatabase.transactions.deleteByObjectId(transaction._id)
    }
  }

  private def stamps(name: String, database: Database): Map[Long, ObjectId] = {
    val pipeline = Seq(
      filter(
        equal("labels", Label.active)
      ),
      project(
        include("stamp")
      )
    )
    val docs = sourceDatabase.routes.aggregate[StampDoc](pipeline)
    val map = docs.map(e => e._id -> e.stamp).toMap
    log.info(s"$name routes: ${map.size}")
    map
  }

  private def delete(sourceMap: Map[Long, ObjectId], targetMap: Map[Long, ObjectId]): Unit = {
    val deletedRouteIds = targetMap.keys.toSet -- sourceMap.keys.toSet
    log.info(s"deleted routes: ${deletedRouteIds.size}")
    deletedRouteIds.foreach { routeId =>
      sourceDatabase.transactions.save(Transaction.routeDelete(routeId))
    }
  }

  private def update(sourceMap: Map[Long, ObjectId], targetMap: Map[Long, ObjectId]): Unit = {
    val updatedRouteIds = sourceMap.keys.toSeq.flatMap { routeId =>
      sourceMap.get(routeId) match {
        case None => Some(routeId)
        case Some(sourceStamp) =>
          targetMap.get(routeId) match {
            case None => Some(routeId)
            case Some(targetStamp) =>
              Option.when(sourceStamp != targetStamp) {
                routeId
              }
          }
      }
    }
    log.info(s"updated routes: ${updatedRouteIds.size}")
    updatedRouteIds.foreach { routeId =>
      sourceDatabase.transactions.save(Transaction.routeUpdate(routeId))
    }
  }
}
