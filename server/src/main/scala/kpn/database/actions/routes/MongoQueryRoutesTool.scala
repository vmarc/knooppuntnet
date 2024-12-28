package kpn.database.actions.routes

import kpn.database.actions.routes.QueryBuilder.andGroup
import kpn.database.actions.routes.QueryBuilder.name
import kpn.database.actions.routes.QueryBuilder.or
import kpn.database.util.Mongo

object MongoQueryRoutesTool {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { database =>
      val query = andGroup(
        or(
          name(
            "LAW 9 - 01"
          ),
          name(
            "LAW 9 - 02"
          )
        )
      )
      val result = new MongoQueryRoutes(database).execute(query)
      result.foreach(println)
      println(s"rows=${result.size}")
    }
  }
}
