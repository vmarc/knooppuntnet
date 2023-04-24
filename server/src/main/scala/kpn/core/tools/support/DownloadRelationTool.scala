package kpn.core.tools.support

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset

object DownloadRelationTool {

  private val routes = Seq(
    "VPSM01" -> 15656714L,
    "VPSM02" -> 15656715L,
    "VPSM03" -> 15656716L,
    "VPSM04" -> 12921288L,
    "VPSM05" -> 12921427L,
    "VPSM06" -> 13014492L,
    "VPSM07" -> 13014677L,
    "VPSM08" -> 13014771L,
    "VPSM09" -> 12955973L,
    "VPSM10" -> 12964641L,
    "VPSM11" -> 12966181L,
    "VPSM12" -> 12966645L,
    "VPSM13" -> 12966812L,
    "VPSM14" -> 12867780L,
    "VPSM15" -> 12882087L,
    "VPSM16" -> 12882250L,
    "VPSM17" -> 12887722L,
    "VPSM18" -> 12859714L,
    "VPSM19" -> 12863114L,
    "VPSM20" -> 12864030L,
    "VPSM21" -> 12865974L,
  )

  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    new DownloadRelationTool(overpassQueryExecutor).download()
    println("down")
  }
}

class DownloadRelationTool(overpassQueryExecutor: OverpassQueryExecutor) {

  def download(): Unit = {
    DownloadRelationTool.routes.foreach { case (name, relationId) =>
      println(s"downloading $name")
      val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelation(relationId))
      val filename = new File(s"/home/vmarc/tmp/xml/$relationId.xml")
      FileUtils.writeStringToFile(filename, xmlString, Charset.forName("UTF-8"))
    }
  }
}
