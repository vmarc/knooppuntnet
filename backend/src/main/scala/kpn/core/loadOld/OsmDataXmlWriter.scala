package kpn.core.loadOld

import java.io.PrintWriter

import kpn.api.common.data.raw.RawData

object OsmDataXmlWriter {
  def write(data: RawData, fileName: String): Unit = {
    val pw = new PrintWriter(fileName)
    try {
      new OsmDataXmlWriter(data, pw).print()
    } finally pw.close()
  }
}

class OsmDataXmlWriter(data: RawData, out: PrintWriter, full: Boolean = true) {

  private val writer = new OsmXmlWriter(out, full)

  def print(): Unit = {
    writer.printHeader()
    printNodes()
    printWays()
    printRelations()
    writer.printFooter()
  }

  private def printNodes(): Unit = {
    data.nodes.sortWith(_.id < _.id).foreach { node =>
      writer.printNode(node)
    }
  }

  private def printWays(): Unit = {
    data.ways.sortWith(_.id < _.id).foreach { way =>
      writer.printWay(way)
    }
  }

  private def printRelations(): Unit = {
    data.relations.sortWith(_.id < _.id).foreach { relation =>
      writer.printRelation(relation)
    }
  }
}
