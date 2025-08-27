package kpn.server.sync

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.util.Mongo
import kpn.server.sync.SyncTool.log
import org.bson.types.ObjectId

object SyncTool {
  private val log = Log(classOf[SyncTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { sourceDatabase =>
      Mongo.webExecuteIn("kpn") { targetDatabase =>
        val tool = new SyncTool(sourceDatabase, targetDatabase)
        // tool.generateInitialTransactions()
        tool.processTransactions()
        // tool.syncRoutes()
      }
    }
  }
}

class SyncTool(sourceDatabase: Database, targetDatabase: Database) {

  private val collections = Seq(
    "routes",
    "nodes",
    "networks",
    //    "pois",

    //    "statistics",
    //    "status",

    //    "monitorGroups",
    //    "monitorRoutes",
    //    "monitorReferences",
    //    "monitorStates",
  )

  def generateInitialTransactions(): Unit = {
    collections.foreach { collection =>
      Log.context(collection) {
        val sourceCollection = sourceDatabase.getCollection(collection)
        sourceCollection.ids().foreach { objectId =>
          sourceDatabase.transactions.save(
            Transaction.update(
              collection,
              objectId
            )
          )
        }
      }
    }
  }

  def processTransactions(): Unit = {
    val transactions = sourceDatabase.transactions.findAll()
    val transactionsCount = transactions.length
    transactions.zipWithIndex.foreach { case (transaction, index) =>
      Log.context(s"${index + 1}/$transactionsCount ${transaction._id}") {
        if (transaction.action == "update") {
          processUpdateTransaction(transaction)
        }
        else if (transaction.action == "delete") {
          processDeleteTransaction(transaction)
        }
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
    log.infoElapsed {
      Log.context(s"${transaction.collection}, ${transaction.id}") {
        try {
          transaction.collection match {
            case "nodes" => processUpdateNode(transaction)
            case "routes" => processUpdateRoute(transaction)
            case "networks" => processUpdateNetwork(transaction)
            case _ => throw new IllegalArgumentException(s"unsupported collection ${transaction.collection}")
          }
        }
        catch {
          case e: Throwable =>
            log.error(s"could not process update", e)
          // throw e
        }
      }

      s"update ${transaction.collection} ${transaction.id}"
    }
  }

  private def processUpdateNode(transaction: Transaction): Unit = {
    sourceDatabase.nodes.findById(transaction.id) match {
      case None => log.warn(s"Could not update node ${transaction.id}")
      case Some(document) =>
        targetDatabase.nodes.save(document)
        sourceDatabase.transactions.deleteByObjectId(transaction._id)
    }
  }

  private def processUpdateRoute(transaction: Transaction): Unit = {
    sourceDatabase.routes.findById(transaction.id) match {
      case None => log.warn(s"Could not update route ${transaction.id}")
      case Some(document) =>
        targetDatabase.routes.save(document)
        sourceDatabase.transactions.deleteByObjectId(transaction._id)
    }
  }

  private def processUpdateNetwork(transaction: Transaction): Unit = {
    sourceDatabase.networks.findById(transaction.id) match {
      case None => log.warn(s"Could not update network ${transaction.id}")
      case Some(document) =>
        targetDatabase.networks.save(document)
        sourceDatabase.transactions.deleteByObjectId(transaction._id)
    }
  }

  private def stamps(name: String, database: Database): Map[Long, ObjectId] = {
    val pipeline = Seq(
      filter(
        equal("active", true),
      ),
      project(
        include("stamp")
      )
    )
    val docs = sourceDatabase.routes.aggregate(pipeline, classOf[StampDoc])
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
