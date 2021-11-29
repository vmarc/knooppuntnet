package kpn.core.tools.location

import kpn.server.json.Json
import org.apache.commons.io.FileUtils

import java.io.File

object LocationBuilderTool {
  def main(args: Array[String]): Unit = {
    new LocationBuilderTool().build()
  }
}

class LocationBuilderTool {

  private val root = "/kpn/locations"

  def build(): Unit = {
    save(s"$root/be", new LocationBuilderBelgium().build())
    save(s"$root/nl", new LocationBuilderNetherlands().build())
    save(s"$root/fr", new LocationBuilderFrance().build())
  }

  private def save(dir: String, datas: Seq[LocationData]): Unit = {
    saveTree(dir, datas)
    saveLocations(dir, datas)
    saveGeometries(dir, datas)
  }

  private def saveTree(dir: String, datas: Seq[LocationData]): Unit = {
    val tree = new NewLocationTreeBuilder().buildTree(datas.map(_.doc))
    prettyWrite(s"$dir/tree.json", tree)
  }

  private def saveLocations(dir: String, datas: Seq[LocationData]): Unit = {
    prettyWrite(s"$dir/locations.json", LocationDocs(datas.map(_.doc)))
  }

  private def saveGeometries(dir: String, datas: Seq[LocationData]): Unit = {
    datas.foreach { data =>
      write(s"$dir/geometries/${data.id}.json", data.geometry.geometry)
    }
  }

  private def write(filename: String, obj: Object): Unit = {
    FileUtils.writeStringToFile(new File(filename), Json.objectMapper.writeValueAsString(obj), "UTF-8")
  }

  private def prettyWrite(filename: String, obj: Object): Unit = {
    val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
    FileUtils.writeStringToFile(new File(filename), json.writeValueAsString(obj), "UTF-8")
  }
}
