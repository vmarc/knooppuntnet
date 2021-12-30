package kpn.database.actions.routes

class MongoQueryRouteChanges(database: Database) {

  private val log = Log(classOf[MongoQueryRouteChanges])

  def execute(routeId: Long, parameters: ChangesParameters): Seq[RouteChange] = {

    val filterElements = Seq(
      Some(equal("key.elementId", routeId)),
      if (parameters.impact) {
        Some(equal("impact", true))
      }
      else {
        None
      },
      parameters.year.map(year => equal("key.time.year", year.toInt)),
      parameters.month.map(month => equal("key.time.month", month.toInt)),
      parameters.day.map(day => equal("key.time.day", day.toInt))
    ).flatten

    val pipeline: Seq[Bson] = Seq(
      filter(
        and(filterElements: _*)
      ),
      sort(
        orderBy(
          descending(
            "key.time.year",
            "key.time.month",
            "key.time.day",
            "key.time.hour",
            "key.time.minute",
            "key.time.second"
          )
        )
      ),
      skip((parameters.itemsPerPage * parameters.pageIndex).toInt),
      limit(parameters.itemsPerPage.toInt),
      project(
        fields(
          excludeId()
        )
      )
    )

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val routeChanges = database.routeChanges.aggregate[RouteChange](pipeline)
      (s"${routeChanges.size} route changes", routeChanges)
    }
  }
}
