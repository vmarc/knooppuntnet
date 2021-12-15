package kpn.core.tools.monitor

object MonitorDemoValidationTool {
  def main(args: Array[String]): Unit = {
    new MonitorDemoValidationTool().validateNonUnique(MonitorDemoRoute.routes)
  }
}


class MonitorDemoValidationTool() {

  def validateNonUnique(demoRoutes: Seq[MonitorDemoRoute]): Unit = {
    val routeMap = demoRoutes.filter(_.routeId > 1).map { route =>
      route.id -> route.filename
    }.groupBy(_._1).map(x => x._1 -> x._2.map(_._2))

    routeMap.foreach { case (routeId, filenames) =>
      if (filenames.size > 1) {
        println(s"$routeId")
        filenames.foreach { filename =>
          println(s"    $filename")
        }
      }
    }
  }

  def gpxValidate(demoRoutes: Seq[MonitorDemoRoute]): Unit = {
    demoRoutes.foreach { demoRoute =>
      val filename = s"/kpn/monitor-demo/${demoRoute.filename}"
      try {
        val geometry = new MonitorRouteGpxReader().read(filename)
        println(s"${geometry.getLength} ${demoRoute.filename}")
      }
      catch {
        case (e: Exception) =>
          println(s"${demoRoute.filename} ${e.getMessage}")
      }
    }
  }
}
