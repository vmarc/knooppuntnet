package kpn.database.actions.routes

import kpn.api.common.search.Condition
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.ConditionGroupOperator
import kpn.api.common.search.ConditionOperator
import kpn.api.common.search.ConditionSubject
import kpn.api.common.search.ConditionTag
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRoutes {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { database =>
      val group = ConditionGroup(
        ConditionGroupOperator.And,
        Seq(
          Condition(
            ConditionSubject.Tag,
            tag = Some(
              ConditionTag(
                ConditionOperator.EndsWith,
                "name",
                "pelgrimspad",
              )
            ),
            location = None,
            name = None,
            group = None,
          )
        )
      )
      val result = new MongoQueryRoutes(database).execute(group)
      result.foreach(println)
      println(s"rows=${result.size}")
    }
  }

  private val log = Log(classOf[MongoQueryRoutes])
}

class MongoQueryRoutes(database: Database) {

  def execute(group: ConditionGroup, log: Log = MongoQueryRoutes.log): Seq[Long] = {
    log.debugElapsed {

      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            SearchQueryBuilder.buildFilter(group)
          )
        ),
        project(
          fields(
            include("_id"),
            computed("tags", "$summary.tags")
          )
        )
      )
      val results = database.routes.aggregate[SearchQueryResult](pipeline, log)
      val ids = SearchQueryPostProcessor.process(group, results).map(_._id)
      (s"${ids.size} routes", ids)
    }
  }
}
