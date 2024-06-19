package kpn.core.loadOld

import org.xml.sax.SAXParseException

import java.io.File
import scala.jdk.CollectionConverters.*
import scala.xml.XML

object XmlFile {

  def load(fileName: String): Seq[scala.xml.Node] = {
    try {
      if (fileName.endsWith(".xml")) {
        Seq(XML.loadFile(new File(fileName)))
      }
      else if (fileName.endsWith(".zip")) {
        val rootzip = new java.util.zip.ZipFile(fileName)
        rootzip.entries.asScala.filter(_.getName.endsWith(".xml")).toSeq.map { e =>
          XML.load(rootzip.getInputStream(e))
        }
      }
      else {
        throw new RuntimeException("Unexpected file extension")
      }
    }
    catch {
      case e: SAXParseException =>
        val message = "Error parsing file \"%s\" at line %d, column %d: %s".format(fileName, e.getLineNumber, e.getColumnNumber, e.getMessage)
        throw new RuntimeException(message, e)
    }
  }
}
