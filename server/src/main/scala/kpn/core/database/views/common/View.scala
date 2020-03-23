package kpn.core.database.views.common

import kpn.core.util.Util.classNameOf
import org.apache.commons.io.IOUtils

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

  def sumAndCount: Option[String] = Some(
    """
      |function(keys, values, rereduce) {
      |  var sum = 0;
      |  var count = 0;
      |  if (rereduce) {
      |    for(var i=0; i < values.length; i++) {
      |      var r = values[i];
      |      sum = sum + r[0];
      |      count = count + r[1];
      |    }
      |  }
      |  else {
      |    for(var i=0; i < values.length; i++) {
      |      sum = sum + values[i];
      |      count = count + 1;
      |    }
      |  }
      |  return [sum, count];
      |}
    """.stripMargin
  )

  private def load(fileType: String): String = {
    val filename = "/" + getClass.getCanonicalName.replaceAll("\\.", "/").replaceAll("\\$", "") + "-" + fileType + ".js"
    IOUtils.toString(this.getClass.getResource(filename), "UTF-8")
  }
}
