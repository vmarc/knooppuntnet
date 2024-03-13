package kpn.core.tools.translations

import org.apache.commons.io.FileUtils
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.traversal.DocumentTraversal
import org.w3c.dom.traversal.NodeFilter

import java.io.File
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object TranslationDeclutterTool {
  def main(args: Array[String]): Unit = {
    TranslationTrimToolOptions.parse(args) foreach { options =>
      new TranslationDeclutterTool(options.root).declutter()
    }
  }
}

class TranslationDeclutterTool(root: String) {

  def declutter(): Unit = {
    declutterFile("translations.en.xlf")
    declutterFile("translations.nl.xlf")
    declutterFile("translations.fr.xlf")
    declutterFile("translations.de.xlf")
  }

  private def declutterFile(filename: String): Unit = {
    val file = new File(s"$root/$filename")
    val doc = readDocument(file)
    declutter(doc)
    writeXmlDocumentToXmlFile(doc, file)
  }

  private def declutter(doc: Document): Unit = {
    var currentTransUnitNode: Option[Node] = None
    val traversal = doc.asInstanceOf[DocumentTraversal]
    val it = traversal.createNodeIterator(doc.getDocumentElement, NodeFilter.SHOW_ELEMENT, null, true)
    var node = it.nextNode
    while (node != null) {
      if (node.getNodeName == "trans-unit") {
        currentTransUnitNode = Some(node)
      }
      else if (node.getNodeName == "context-group") {
        currentTransUnitNode match {
          case Some(translationUnit) => translationUnit.removeChild(node)
          case None =>
        }
      }
      node = it.nextNode
    }
  }

  private def readDocument(file: File): Document = {
    if (!file.exists()) {
      throw new RuntimeException("translations file not found: " + file.getAbsolutePath)
    }
    val factory = DocumentBuilderFactory.newInstance
    factory.newDocumentBuilder.parse(file)
  }

  private def writeXmlDocumentToXmlFile(doc: Document, file: File): Unit = {
    val tf = TransformerFactory.newInstance
    val transformer = tf.newTransformer
    val writer = new StringWriter
    transformer.transform(new DOMSource(doc), new StreamResult(writer))
    val xmlString = writer.getBuffer.toString
    val lines = xmlString.split("\n")
    val whithoutBlankLines = lines.filter(line => line.trim.length > 0).mkString("\n")
    FileUtils.writeStringToFile(file, whithoutBlankLines, "UTF-8")
  }
}
