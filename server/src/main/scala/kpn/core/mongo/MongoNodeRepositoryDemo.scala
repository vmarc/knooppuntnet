package kpn.core.mongo

import kpn.api.custom.NetworkType

object MongoNodeRepositoryDemo {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "tryout")
      val repository = new MongoNodeRepositoryImpl(database)
      val demo = new MongoNodeRepositoryDemo(repository)
      // demo.findNodesByLocation()
      demo.findNodesByLocationBelgium()
      // demo.findNetworkReferences()
      // demo.findRouteReferences()
      // demo.nodeCountPerSubset()
    }
    finally {
      mongoClient.close()
    }
  }
}

class MongoNodeRepositoryDemo(repository: MongoNodeRepositoryImpl) {

  def findNodesByLocation(): Unit = {
    println("nodes by location")
    val networkType = NetworkType.hiking
    repository.findNodesByLocation(networkType, "Essen BE", 0, 1)
    val (total, nodeInfos) = repository.findNodesByLocation(networkType, "Essen BE", 0, 100)
    println(s"location total node count: $total")
    nodeInfos.zipWithIndex.foreach { case (nodeDoc2, index) =>
      println(s"  ${index + 1} name: ${nodeDoc2.node.networkTypeName(networkType)}, id: ${nodeDoc2.node.id}")
      nodeDoc2.routeRefs.foreach { nodeRouteRef =>
        println(s"    ${nodeRouteRef.routeName}")
      }
    }
  }

  def findNodesByLocationBelgium(): Unit = {
    println("nodes by location")
    val networkType = NetworkType.hiking
    repository.findNodesByLocation(networkType, "be", 0, 1)
    val (total, nodeInfos) = repository.findNodesByLocation(networkType, "be", 400, 25)
    println(s"location total node count: $total")
    nodeInfos.zipWithIndex.foreach { case (nodeDoc2, index) =>
      println(s"  ${index + 1} name: ${nodeDoc2.node.networkTypeName(networkType)}, id: ${nodeDoc2.node.id}, survey: ${nodeDoc2.node.lastSurvey}")
      nodeDoc2.routeRefs.foreach { nodeRouteRef =>
        println(s"    ${nodeRouteRef.routeName}")
      }
    }
  }

  def findNetworkReferences(): Unit = {
    println("find network references")
    val refs = repository.findNetworkReferences(3010119246L)
    refs.zipWithIndex.foreach { case (ref, index) =>
      println(s"  ${index + 1} ${ref.id} ${ref.name}")
    }
  }

  def findRouteReferences(): Unit = {
    println("find route references")
    val refs = repository.findRouteReferences(3010119246L)
    refs.zipWithIndex.foreach { case (ref, index) =>
      println(s"  ${index + 1} ${ref.id} ${ref.name}")
    }
  }

  def nodeCountPerSubset(): Unit = {
    val subsetNodeCounts = repository.nodeCountPerSubset()
    subsetNodeCounts.foreach(println)
  }
}
