package kpn.core.db.views

import kpn.core.util.Util.classNameOf
import org.apache.commons.io.IOUtils
import spray.json.JsValue

trait View {
  def name: String = classNameOf(this)

  def map: String = {
    s"function(doc) { /* map $name */ " +
      load("map") +
      "}"
  }

  def reduce: Option[String] = {
    Some(
      s"function(keys, values, rereduce) { /* reduce $name */ \n" +
        load("reduce") + "\n" +
        "return result;\n" +
        "}"
    )
  }

  def toRow(row: JsValue): ViewRow = new ViewRow(row)

  private def load(fileType: String): String = {
    val filename = "/" + getClass.getCanonicalName.replaceAll("\\.", "/").replaceAll("\\$", "") + "-" + fileType + ".js"
    IOUtils.toString(this.getClass.getResource(filename), "UTF-8")
  }
}
