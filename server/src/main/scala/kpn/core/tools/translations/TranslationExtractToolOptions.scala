package kpn.core.tools.translations

import kpn.database.base.Options

object TranslationExtractToolOptions extends Options[TranslationExtractToolOptions] {

  def parse(args: Array[String]): Option[TranslationExtractToolOptions] = {
    optionParser.parse(args, TranslationExtractToolOptions())
  }

  private def optionParser: scopt.OptionParser[TranslationExtractToolOptions] = {
    new scopt.OptionParser[TranslationExtractToolOptions]("TranslationExtractToolOptions") {
      opt[String]('r', "root").required() valueName "<directory>" action { (x, c) =>
        c.copy(root = x)
      } text "frontend source root directory name"
    }
  }
}

case class TranslationExtractToolOptions(
  root: String = ""
)
