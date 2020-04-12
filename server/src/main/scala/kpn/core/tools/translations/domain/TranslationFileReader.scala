package kpn.core.tools.translations.domain

import scala.xml.Node
import scala.xml.XML

class TranslationFileReader {

  def read(filename: String): TranslationFile = {
    val xml = XML.load(filename)
    val fileNode = xml.child(1)
    toTranslationFile(fileNode)
  }

  private def toTranslationFile(fileNode: Node) = {

    val sourceLanguage = (fileNode \ "@source-language").text
    val targetLanguage = (fileNode \ "@target-languate").text
    val translationUnits = {
      val body = fileNode \ "body"
      (body \\ "trans-unit").map(toTranslationUnit)
    }

    TranslationFile(
      sourceLanguage,
      targetLanguage,
      translationUnits
    )
  }

  private def toTranslationUnit(translationUnit: Node) = {

    val id = (translationUnit \ "@id").text
    val source = (translationUnit \ "source").text
    val targetNode = translationUnit \ "target"
    val target = targetNode.text
    val state = (targetNode \ "@state").text
    val locations = (translationUnit \\ "context-group").map(toTranslationLocation)

    TranslationUnit(
      id,
      source,
      target,
      if (state.isEmpty) None else Some(state),
      locations
    )
  }

  private def toTranslationLocation(contextGroup: Node) = {

    val contexts = (contextGroup \ "context").map { context =>
      val contextType = (context \ "@context-type").text
      val value = context.text
      contextType -> value
    }.toMap

    val sourceFile = contexts("sourcefile")
    val lineNumber = contexts("linenumber").toInt
    TranslationLocation(sourceFile, lineNumber)
  }
}
