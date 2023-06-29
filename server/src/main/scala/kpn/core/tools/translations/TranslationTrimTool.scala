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

object TranslationTrimTool {
  def main(args: Array[String]): Unit = {
    TranslationTrimToolOptions.parse(args) foreach { options =>
      new TranslationTrimTool(options.root).trim()
    }
  }
}

class TranslationTrimTool(root: String) {

  def trim(): Unit = {
    val doc = readDocument()
    trimNodes(doc)
    writeXmlDocumentToXmlFile(doc)
  }

  def trimNodes(doc: Document): Unit = {
    val traversal = doc.asInstanceOf[DocumentTraversal]
    val it = traversal.createNodeIterator(doc.getDocumentElement, NodeFilter.SHOW_ELEMENT, null, true)
    var node = it.nextNode
    while (node != null) {
      if (node.getNodeName == "source" || node.getNodeName == "target") {
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
      throw new RuntimeException("translations file not found: " + file.getAbsolutePath)
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
    val file = new File(s"$root/locale/translations.trimmed.xlf")
    println("write " + file.getAbsolutePath)
    FileUtils.writeStringToFile(file, xmlString, "UTF-8")
  }
}
