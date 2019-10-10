package kpn.core.loadOld

import java.io.File

import scala.xml.XML

import org.xml.sax.SAXParseException

object XmlFile {

  def load(fileName: String): Seq[scala.xml.Node] = {
    try {
      if (fileName.endsWith(".xml")) {
        Seq(XML.loadFile(new File(fileName)))
      }
      else if (fileName.endsWith(".zip")) {
        val rootzip = new java.util.zip.ZipFile(fileName)
        import scala.collection.JavaConversions._
        rootzip.entries.filter(_.getName.endsWith(".xml")).toSeq.map { e =>
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
