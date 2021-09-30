package kpn.database.tools

import kpn.database.util.Mongo

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ListDatabaseNames {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    val databaseNamesFuture = mongoClient.listDatabaseNames().toFuture()
    val databaseNames = Await.result(databaseNamesFuture, Duration(10, TimeUnit.SECONDS))
    println("Database names")
    databaseNames.foreach { databaseName =>
      println(s"  $databaseName")
    }
  }
}
