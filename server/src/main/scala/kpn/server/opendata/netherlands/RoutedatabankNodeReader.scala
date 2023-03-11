package kpn.server.opendata.netherlands

import kpn.core.util.Log

import java.io.FileInputStream
import java.io.InputStream

object RoutedatabankNodeReader {
  def main(args: Array[String]): Unit = {
    val filename = "/kpn/opendata/netherlands/Wandelknooppunten (wgs84).json"
    val inputStream = new FileInputStream(filename)
    new RoutedatabankNodeReader().read(inputStream)
  }
}

class RoutedatabankNodeReader {

  private val log = Log(classOf[RoutedatabankNodeReader])

  def read(inputStream: InputStream): Seq[RoutedatabankNode] = {
    val nodes = new RoutedatabankNodeParser().parse(inputStream)

    log.info(s"nodes: ${nodes.size}")

    val nodeTypeMap = nodes.groupBy(_.nodeType)
    nodeTypeMap.keys.foreach { key =>
      log.info(s"""node type "$key": ${nodeTypeMap(key).size}""")
    }

    val updatedValues = nodes.flatMap(_.updated)
    val firstUpdate = updatedValues.min
    val lastUpdate = updatedValues.max

    log.info(s"firstUpdate: $firstUpdate")
    log.info(s"lastUpdate: $lastUpdate")

    val idMap = nodes.groupBy(_._id)
    val nonUniqueIds = idMap.keys.filter { key =>
      idMap(key).size > 1
    }
    log.info(s"nonUniqueIds: ${nonUniqueIds.size}")
    nonUniqueIds.foreach { id =>
      log.info(s"  id=$id")
    }

    nodes
  }
}
