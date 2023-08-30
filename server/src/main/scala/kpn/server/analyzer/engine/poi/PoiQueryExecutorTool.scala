package kpn.server.analyzer.engine.poi

import kpn.core.overpass.OverpassQueryExecutorRemoteImpl

object PoiQueryExecutorTool {
  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val poiQueryExecutor = new PoiQueryExecutorImpl(overpassQueryExecutor)
    new PoiQueryExecutorTool(poiQueryExecutor).run()
  }
}

class PoiQueryExecutorTool(poiQueryExecutor: PoiQueryExecutor) {

  def run(): Unit = {

    val wayIds = Seq(
      18017569L,
      18033924L,
      18077882L,
      20782319L,
      20782331L,
    )

    val relationIds = Seq(
      1418059L,
      1418377L,
      1454905L,
      1456239L,
      1538532L,
    )

    val wayCenters1 = wayIds.flatMap(id =>
      poiQueryExecutor.center(PoiRef("way", id)).map(ElementCenter(id, _))
    )
    val relationCenters1 = relationIds.flatMap(id =>
      poiQueryExecutor.center(PoiRef("relation", id)).map(center => ElementCenter(id, center))
    )

    val wayCenters2 = poiQueryExecutor.centers("way", wayIds)
    val relationCenters2 = poiQueryExecutor.centers("relation", relationIds)

    println(s"wayCenters1: $wayCenters1")
    println(s"wayCenters2: $wayCenters2")

    println(s"relationCenters1: $relationCenters1")
    println(s"relationCenters2: $relationCenters2")

    println("waysCenters same: " + wayCenters1.equals(wayCenters2))
    println("relationCenters same: " + relationCenters1.equals(relationCenters2))
  }
}
