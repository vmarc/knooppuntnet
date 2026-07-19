package kpn.server.opendata.netherlands

import kpn.core.tools.config.Dirs
import kpn.core.util.Log

import java.io.FileInputStream
import java.io.InputStream

object RoutedatabankRouteReader {
  def main(args: Array[String]): Unit = {
    val filename = s"${Dirs.root}/opendata/netherlands/Wandelnetwerken (wgs84).json"
    val inputStream = new FileInputStream(filename)
    new RoutedatabankRouteReader().read(inputStream)
  }
}

class RoutedatabankRouteReader {

  private val log = Log(classOf[RoutedatabankRouteReader])

  def read(inputStream: InputStream): Seq[RoutedatabankRoute] = {
    val routes = new RoutedatabankRouteParser().parse(inputStream)

    log.info(s"routes: ${routes.size}")

    val updatedValues = routes.flatMap(_.updated)
    val firstUpdate = updatedValues.min
    val lastUpdate = updatedValues.max

    log.info(s"firstUpdate: $firstUpdate")
    log.info(s"lastUpdate: $lastUpdate")

    val idMap = routes.groupBy(_._id)
    val nonUniqueIds = idMap.keys.filter { key =>
      idMap(key).sizeIs > 1
    }
    log.info(s"nonUniqueIds: ${nonUniqueIds.size}")
    nonUniqueIds.foreach { id =>
      log.info(s"  id=$id")
    }

    routes
  }
}
