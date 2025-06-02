package kpn.core.tools.translations

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import kpn.database.base.Options
import kpn.database.base.Tool

import java.io.File
import scala.collection.SortedMap

/*
  Reformats the translations files exported from POEditor to what Angular expects.
 */
object TranslationImportTool extends Tool[TranslationImportToolOptions] {

  override def options: Options[TranslationImportToolOptions] = TranslationImportToolOptions

  override def execute(options: TranslationImportToolOptions): Unit = {
    new TranslationImportTool(options.root).process()
  }
}

class TranslationImportTool(root: String) {

  private val home = System.getProperty("user.home")
  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  def process(): Unit = {
    Seq("de", "en", "fr", "nl").foreach(processLocale)
    println("done")
  }

  private def processLocale(locale: String): Unit = {
    val translations = readTranslations(locale)
    val transformed = transformTranslations(locale, translations)
    writeTranslations(locale, transformed)
  }

  private def readTranslations(language: String): Map[String, String] = {
    val file = inputFile(language)
    mapper.readValue(file, classOf[Map[String, String]])
  }

  private def inputFile(language: String): File = {
    val file = new File(s"$home/Downloads/translations.$language.json")
    if (!file.exists()) {
      throw new RuntimeException(s"translations file not found: ${file.getAbsolutePath}")
    }
    file
  }

  private def transformTranslations(locale: String, translations: Map[String, String]): Map[String, _] = {
    val sortedTranslations = SortedMap[String, String]() ++ translations
    Map(
      "locale" -> locale,
      "translations" -> sortedTranslations
    )
  }

  private def writeTranslations(locale: String, transformed: Map[String, _]): Unit = {
    val file = new File(s"$root/locale/translations.$locale.json")
    mapper.writerWithDefaultPrettyPrinter.writeValue(file, transformed)
  }
}
