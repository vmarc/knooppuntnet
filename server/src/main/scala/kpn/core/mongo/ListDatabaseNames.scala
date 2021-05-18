package kpn.core.mongo

import org.mongodb.scala.MongoClient

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ListDatabaseNames {
  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClient("mongodb://kpn-tiles:27017")
    val databaseNamesFuture = mongoClient.listDatabaseNames().toFuture()
    val databaseNames = Await.result(databaseNamesFuture, Duration(10, TimeUnit.SECONDS))
    println("Database names")
    databaseNames.foreach { databaseName =>
      println(s"  $databaseName")
    }
  }
}
