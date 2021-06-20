package kpn.core.mongo

import kpn.api.custom.NetworkType
import kpn.core.mongo.util.Mongo

object MongoNodeRepositoryDemo {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val repository = new MongoNodeRepositoryImpl(database)
      val demo = new MongoNodeRepositoryDemo(repository)
      demo.findNetworkReferences()
      demo.findRouteReferences()
    }
  }
}

class MongoNodeRepositoryDemo(repository: MongoNodeRepositoryImpl) {

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
}
