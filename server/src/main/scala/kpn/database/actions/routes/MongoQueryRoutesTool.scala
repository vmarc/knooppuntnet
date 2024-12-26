package kpn.database.actions.routes

import kpn.database.actions.routes.QueryBuilder.and
import kpn.database.actions.routes.QueryBuilder.tagContains
import kpn.database.util.Mongo

object MongoQueryRoutesTool {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { database =>
      val query = and(
        tagContains(
          "name",
          "pelgrimspad",
        )
      )
      val result = new MongoQueryRoutes(database).execute(query)
      result.foreach(println)
      println(s"rows=${result.size}")
    }
  }
}
