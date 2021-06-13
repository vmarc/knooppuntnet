package kpn.core.mongo

import kpn.core.mongo.util.Mongo

object MongoNetworkRepositoryDemo {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val repository = new MongoNetworkRepositoryImpl(database)
      val demo = new MongoNetworkRepositoryDemo(repository)
      demo.networkCount()
      demo.findAllNetworks()
      demo.findNetworkById()
      demo.findNetworkByIdNotFound()
      demo.findNetworkByName()
      demo.findNetworkByNameNotFound()
    }
  }
}

class MongoNetworkRepositoryDemo(repository: MongoNetworkRepositoryImpl) {

  def networkCount(): Unit = {
    val networkCount = repository.networkCount()
    println(s"networkCount: $networkCount")
  }

  def findAllNetworks(): Unit = {
    println("find all networks")
    val networkNames = repository.findAllNetworks()
    println(s"network names")
    networkNames.foreach { name =>
      println(s"  $name")
    }
  }

  def findNetworkById(): Unit = {
    println("find network by id")
    repository.findNetworkById(1066154L) match {
      case None => println("  NOK - network not found")
      case Some(networkDoc) =>
        val attributes = networkDoc.network.attributes
        println(s"  networkScope: ${attributes.networkScope.name}")
        println(s"  networkType: ${attributes.networkType.name}")
        println(s"  name: ${attributes.name}")
    }
  }

  def findNetworkByIdNotFound(): Unit = {
    println("find network by id")
    repository.findNetworkById(0L) match {
      case Some(_) => println("  NOK - network found")
      case None => println("  OK - Network not found as expected")
    }
  }

  def findNetworkByName(): Unit = {
    println("find network by name")
    repository.findNetworkByName("Tholen") match {
      case None => println("  NOK - network not found")
      case Some(networkDoc) =>
        val attributes = networkDoc.network.attributes
        println(s"  networkScope: ${attributes.networkScope.name}")
        println(s"  networkType: ${attributes.networkType.name}")
        println(s"  name: ${attributes.name}")
    }
  }

  def findNetworkByNameNotFound(): Unit = {
    println("find non-existing network by name")
    repository.findNetworkByName("bla") match {
      case Some(_) => println("  NOK - network found")
      case None => println("  OK - Network not found as expected")
    }
  }
}
