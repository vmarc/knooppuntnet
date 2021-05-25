package kpn.core.mongo

import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.model.Indexes

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CreateIndexes {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    val database = mongoClient.getDatabase("tryout")
    val createIndexes = new CreateIndexes(database)
    // createIndexes.createIndexNetworkName()
    // createIndexes.createIndexNetworkNodeReferences()
    /// createIndexes.createIndexNodeLocations()
    // createIndexes.createIndexNodeActive()
    // createIndexes.createIndexRouteNodeReferences()
    createIndexes.createIndexRouteNodeRefs()
    mongoClient.close()
  }
}

class CreateIndexes(database: MongoDatabase) {

  def createIndexNetworkName(): Unit = {
    val networks = database.getCollection("networks")
    val future = networks.createIndex(
      Indexes.ascending("network.active", "network.attributes.name"),
      IndexOptions().name("network-name")
    ).toFuture()
    val result = Await.result(future, Duration(3, TimeUnit.MINUTES))
    println(s"Created index: $result")
  }

  def createIndexNetworkNodeReferences(): Unit = {
    val networks = database.getCollection("networks")
    val future = networks.createIndex(
      Indexes.ascending("network.active", "network.nodeRefs"),
      IndexOptions().name("network-node-references")
    ).toFuture()
    val result = Await.result(future, Duration(3, TimeUnit.MINUTES))
    println(s"Created index: $result")
  }

  def createIndexNodeLocations(): Unit = {
    val nodes = database.getCollection("nodes")
    val future = nodes.createIndex(
      Indexes.ascending("node.active", "node.location.names"),
      IndexOptions().name("location")
    ).toFuture()
    val result = Await.result(future, Duration(3, TimeUnit.MINUTES))
    println(s"Created index: $result")
  }

  def createIndexNodeActive(): Unit = {
    val nodes = database.getCollection("nodes")
    val future = nodes.createIndex(
      Indexes.ascending("node.active"),
      IndexOptions().name("node-active")
    ).toFuture()
    val result = Await.result(future, Duration(3, TimeUnit.MINUTES))
    println(s"Created index: $result")
  }


  def createIndexRouteNodeReferences(): Unit = {
    val networks = database.getCollection("routes")
    val future = networks.createIndex(
      Indexes.ascending("route.active", "route.nodeRefs"),
      IndexOptions().name("route-node-references")
    ).toFuture()
    val result = Await.result(future, Duration(3, TimeUnit.MINUTES))
    println(s"Created index: $result")
  }

  def createIndexRouteNodeRefs(): Unit = {
    val networks = database.getCollection("nodeRouteRefs")
    val future = networks.createIndex(
      Indexes.ascending("nodeId", "routeName"),
      IndexOptions().name("nodeId")
    ).toFuture()
    val result = Await.result(future, Duration(3, TimeUnit.MINUTES))
    println(s"Created index: $result")
  }
}