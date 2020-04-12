package kpn.core.tools.translations.domain

object Trim {
  def trim(before: String): String = {
    before.trim.filter(_ != '\n').replaceAll(" +", " ")
  }
}
