package kpn.api.common.common

case class ToStringBuilder(className: String, strings: Seq[String] = Seq.empty) {

  def field(name: String, value: Any): ToStringBuilder = {
    val valueString = value match {
      case string: String => s""""$string""""
      case _ => value
    }
    value match {
      case coll: Seq[Any] => collection(name, coll)
      case _ =>
        val fieldString = s"$name = $valueString"
        val splittedString = fieldString.split("\n").toSeq
        val indentedStrings = splittedString.mkString("  ", "\n  ", "")
        copy(strings = strings :+ indentedStrings)
    }
  }

  def fieldValue(name: String, value: Any): ToStringBuilder = {
    val fieldString = s"$name = $value"
    val splittedString = fieldString.split("\n").toSeq
    val indentedStrings = splittedString.mkString("  ", "\n  ", "")
    copy(strings = strings :+ indentedStrings)
  }

  def optionalCollection(name: String, coll: Seq[Any]): ToStringBuilder = {
    if (coll.nonEmpty) {
      collection(name, coll)
    }
    else {
      this
    }
  }

  private def collection(name: String, coll: Seq[Any]): ToStringBuilder = {
    if (coll.isEmpty) {
      fieldValue(name, s"Seq()")
    }
    else {
      val elementString = coll.map(_.toString).mkString(",\n")
      val splittedStrings = elementString.split("\n").toSeq
      val indentedStrings = splittedStrings.mkString("  ", "\n  ", "")
      fieldValue(name, s"Seq(\n$indentedStrings\n)")
    }
  }

  def build: String = {
    val detail = if (strings.isEmpty) "" else strings.mkString("\n", "\n", "\n")
    val string = s"$className($detail)"
    string.replace("List(", "Seq(").replace("ArrayBuffer(", "Seq(").replace("ListBuffer(", "Seq(")
  }
}
