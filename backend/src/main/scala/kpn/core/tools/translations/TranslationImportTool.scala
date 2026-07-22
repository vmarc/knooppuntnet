package kpn.core.tools.translations

import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.server.json.Json

import java.io.File
import java.io.FileReader
import java.io.FileWriter

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

  def process(): Unit = {
    Seq("de", "en", "fr", "nl").foreach(processLocale)
    println("done")
  }

  private def processLocale(locale: String): Unit = {
    val poeTranslations = readPoeTranslations(locale)
    val translations = Translations(locale, poeTranslations.translations)
    writeTranslations(locale, translations)
  }

  private def readPoeTranslations(language: String): PoeTranslations = {
    val file = inputFile(language)
    val reader = new FileReader(file)
    try {
      Json.readValue(reader, classOf[PoeTranslations])
    }
    finally {
      reader.close()
    }
  }

  private def inputFile(language: String): File = {
    val file = new File(s"$home/Downloads/translations.$language.json")
    if (!file.exists()) {
      throw new RuntimeException(s"translations file not found: ${file.getAbsolutePath}")
    }
    file
  }

  private def writeTranslations(locale: String, translations: Translations): Unit = {
    val filename = s"$root/locale/translations.$locale.json"
    println(s"writing $filename")
    val file = new File(filename)
    val writer = new FileWriter(file)
    try {
      writer.write(Json.pretty(translations))
    }
    finally {
      writer.close()
    }
  }
}
