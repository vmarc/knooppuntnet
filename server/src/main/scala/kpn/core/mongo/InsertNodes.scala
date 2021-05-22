package kpn.core.mongo

import kpn.core.database.Database
import kpn.core.database.doc.NodeDoc
import kpn.core.db.couch.Couch
import kpn.server.repository.NodeRepositoryImpl
import org.mongodb.scala.MongoClient

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object InsertNodes {

  def main(args: Array[String]): Unit = {
    println("Insert nodes")
    Couch.executeIn("kpn-database", "analysis") { couchDatabase =>
      migrate(couchDatabase)
      println("Done")
    }
  }

  private def migrate(couchDatabase: Database): Unit = {
    val mongoClient = MongoClient("mongodb://kpn-tiles:27017")
    val database = Mongo.database(mongoClient, "tryout")
    val nodesCollection = database.getCollection[NodeDoc]("nodes")
    val repo = new NodeRepositoryImpl(couchDatabase)
    val allNodeIds = repo.allNodeIds()
    println(s"Collecting details of ${allNodeIds.size} nodes")
    allNodeIds.sliding(100, 100).foreach { nodeIds =>
      val nodeInfos = repo.nodesWithIds(nodeIds)
      val nodeDocs = nodeInfos.map(n => NodeDoc(s"node:${n.id}", n))
      val insertManyResultFuture = nodesCollection.insertMany(nodeDocs).toFuture()
      val insertManyResult = Await.result(insertManyResultFuture, Duration(1, TimeUnit.MINUTES))
      println(s"Insert acknowledged: ${insertManyResult.wasAcknowledged}, ${nodeIds.size} nodes")
    }
    mongoClient.close()
  }
}
