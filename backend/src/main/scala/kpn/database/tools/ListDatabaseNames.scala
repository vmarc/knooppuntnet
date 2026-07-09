package kpn.database.tools

import kpn.database.util.Mongo

import scala.jdk.CollectionConverters.IterableHasAsScala

object ListDatabaseNames {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    val databaseNames = mongoClient.listDatabaseNames().asScala.toSeq
    println("Database names")
    databaseNames.foreach { databaseName =>
      println(s"  $databaseName")
    }
  }
}
