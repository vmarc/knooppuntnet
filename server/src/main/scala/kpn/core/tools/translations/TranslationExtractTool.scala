package kpn.core.tools.translations

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import kpn.database.base.Options
import kpn.database.base.Tool

import java.io.File
import scala.collection.SortedMap

/*
  Postprocesses the translations file that is extracted from the Angular code
  to prepare for import in POEditor.
 */
object TranslationExtractTool extends Tool[TranslationExtractToolOptions] {

  override def options: Options[TranslationExtractToolOptions] = TranslationExtractToolOptions

  override def execute(options: TranslationExtractToolOptions): Unit = {
    new TranslationExtractTool(options.root).process()
  }
}

class TranslationExtractTool(root: String) {

  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  def process(): Unit = {
    val translations = loadSourceTranslations()
    val transformedTranslations = sortAndTrimTranslations(translations)
    saveProcessedTranslations(transformedTranslations)
  }

  private def sortAndTrimTranslations(translations: Map[String, String]): SortedMap[String, String] = {
    val trimmedTranslations = translations.toSeq.map { case (key, value) =>
      (key, value.trim)
    }
    SortedMap[String, String]() ++ trimmedTranslations
  }

  private def loadSourceTranslations(): Map[String, String] = {
    val file = sourceTranslationsFile()
    val jsonMap = mapper.readValue(file, classOf[Map[String, Any]])
    jsonMap("translations").asInstanceOf[Map[String, String]]
  }

  private def sourceTranslationsFile(): File = {
    val file = new File(s"$root/locale/translations-extract.json")
    if (!file.exists()) {
      throw new RuntimeException(s"extracted translations file not found: ${file.getAbsolutePath}")
    }
    file
  }

  private def saveProcessedTranslations(transformedTranslations: SortedMap[String, String]): Unit = {
    val file = new File(s"$root/locale/translations.json")
    mapper.writerWithDefaultPrettyPrinter.writeValue(file, transformedTranslations)
  }
}
