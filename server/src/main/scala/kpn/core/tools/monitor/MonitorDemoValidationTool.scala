package kpn.core.tools.monitor

object MonitorDemoValidationTool {
  def main(args: Array[String]): Unit = {
    new MonitorDemoValidationTool().gpxValidate(MonitorDemoRoute.routes)
  }
}


class MonitorDemoValidationTool() {

  def validateNonUnique(demoRoutes: Seq[MonitorDemoRoute]): Unit = {
    val routeMap = demoRoutes.map { route =>
      route.name -> route.filename
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
      val filename = s"/kpn/monitor/GR-2022-08-12/Tracés GPX/${demoRoute.filename}.gpx"
      try {
        val geometry = new MonitorRouteGpxReader().readFile(filename)
        println(s"${geometry.getLength} ${demoRoute.filename}")
      }
      catch {
        case (e: Exception) =>
          println(s"${demoRoute.filename} ${e.getMessage}")
      }
    }
  }
}
