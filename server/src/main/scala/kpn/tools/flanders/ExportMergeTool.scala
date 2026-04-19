package kpn.tools.flanders

import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.core.common.Time
import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.loadOld.OsmDataXmlWriter
import kpn.core.util.Log

import java.io.File
import scala.collection.mutable

class ExportData {
  val nodes = mutable.Map[Long, RawNode]()
  val ways = mutable.Map[Long, RawWay]()
  val relations = mutable.Map[Long, RawRelation]()

  def toRawData: RawData = {
    RawData(
      Some(Time.now),
      nodes.values.toSeq,
      ways.values.toSeq,
      relations.values.toSeq
    )
  }
}

object ExportMergeTool {
  def main(args: Array[String]): Unit = {
    new ExportMergeTool().merge()
  }
}

class ExportMergeTool {

  private val log = Log(classOf[ExportMergeTool])

  def merge(): Unit = {
    log.info("merge start")
    val all = new File("/Users/marc/kpn/export")
    exportDir(all, "all")
    all.listFiles().filter(_.isDirectory).foreach { province =>
      exportDir(province, province.getName)
    }
  }

  private def exportDir(dir: File, name: String): Unit = {
    log.info(s"export $name")
    val data = new ExportData()
    mergeDir(data, dir)
    OsmDataXmlWriter.write(data.toRawData, s"/Users/marc/kpn/export-$name.xml")
  }

  private def mergeDir(data: ExportData, dir: File): Unit = {
    log.info(s"merge ${dir.getAbsolutePath}")
    val files = dir.listFiles()
    files.filter(_.getName.endsWith(".xml")).foreach(f => mergeXml(data, f))
    files.filter(_.isDirectory).foreach(f => mergeDir(data, f))
  }

  private def mergeXml(exportData: ExportData, xmlFile: File): Unit = {
    val data = OsmDataXmlReader.read(xmlFile.getAbsolutePath)
    exportData.nodes ++= data.nodes.map(n => n.id -> n)
    exportData.ways ++= data.ways.map(w => w.id -> w)
    exportData.relations ++= data.relations.map(r => r.id -> r)
  }
}
