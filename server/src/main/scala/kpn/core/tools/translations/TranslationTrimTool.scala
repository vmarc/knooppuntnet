package kpn.core.tools.translations

import kpn.database.base.Options
import kpn.database.base.Tool
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

object TranslationTrimTool extends Tool[TranslationTrimToolOptions] {

  override def options: Options[TranslationTrimToolOptions] = TranslationTrimToolOptions

  override def execute(options: TranslationTrimToolOptions): Unit = {
    new TranslationTrimTool(options.root).trim()
  }
}

class TranslationTrimTool(root: String) {

  def trim(): Unit = {
    val doc = readDocument()
    trimNodes(doc)
    writeXmlDocumentToXmlFile(doc)
  }

  def trimNodes(doc: Document): Unit = {
    var currentTransUnitNode: Option[Node] = None
    val traversal = doc.asInstanceOf[DocumentTraversal]
    val it = traversal.createNodeIterator(doc.getDocumentElement, NodeFilter.SHOW_ELEMENT, null, true)
    var node = it.nextNode
    while (node != null) {
      if (node.getNodeName == "trans-unit") {
        currentTransUnitNode = Some(node)
      }
      else if (node.getNodeName == "context-group") {
        currentTransUnitNode.foreach(_.removeChild(node))
      }
      else if (node.getNodeName == "source" || node.getNodeName == "target") {
        trimNode(node)
      }
      node = it.nextNode
    }
  }

  private def trimNode(node: Node): Unit = {
    val children = node.getChildNodes
    val firstChild = children.item(0)
    if (firstChild.getNodeType == Node.TEXT_NODE) {
      val first = firstChild.getTextContent
      val firstTrimmed = first.dropWhile(c => Character.isWhitespace(c) || c == '\n')
      firstChild.setTextContent(firstTrimmed)
    }
    if (children.getLength > 0) {
      val lastChild = children.item(children.getLength - 1)
      if (lastChild.getNodeType == Node.TEXT_NODE) {
        val last = lastChild.getTextContent
        val lastTrimmed = last.reverse.dropWhile(c => Character.isWhitespace(c) || c == '\n').reverse
        lastChild.setTextContent(lastTrimmed)
      }
    }
  }

  private def readDocument(): Document = {
    val file = new File(s"$root/locale/translations.xlf")
    if (!file.exists()) {
      throw new RuntimeException(s"translations file not found: ${file.getAbsolutePath}")
    }
    val factory = DocumentBuilderFactory.newInstance
    factory.newDocumentBuilder.parse(file)
  }

  private def writeXmlDocumentToXmlFile(doc: Document): Unit = {
    val tf = TransformerFactory.newInstance
    val transformer = tf.newTransformer
    val writer = new StringWriter
    transformer.transform(new DOMSource(doc), new StreamResult(writer))
    val xmlString = writer.getBuffer.toString
    val lines = xmlString.split("\n")
    val whithoutBlankLines = lines.filter(line => line.trim.nonEmpty).mkString("\n")
    val file = new File(s"$root/locale/translations.trimmed.xlf")
    println(s"write ${file.getAbsolutePath}")
    FileUtils.writeStringToFile(file, whithoutBlankLines, "UTF-8")
  }
}
