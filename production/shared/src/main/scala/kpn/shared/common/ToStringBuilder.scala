package kpn.shared.common

case class ToStringBuilder(className: String, strings: Seq[String] = Seq()) {

  def field(name: String, value: Any): ToStringBuilder = {
    val fieldString = s"$name = $value"
    val splittedString = fieldString.split("\n").toSeq
    val indentedStrings = splittedString.mkString("  ", "\n  ", "")
    copy(strings = strings :+ indentedStrings)
  }

  def optionalCollection(name: String, coll: Seq[Any]): ToStringBuilder = {
    if (coll.nonEmpty) {
      val elementString = coll.map(_.toString).mkString(",\n")
      val splittedStrings = elementString.split("\n").toSeq
      val indentedStrings = splittedStrings.mkString("  ", "\n  ", "")
      field(name, s"Seq(\n$indentedStrings\n)")
    }
    else {
      this
    }
  }

  def build: String = {
    val detail = if (strings.isEmpty) "" else strings.mkString("\n", "\n", "\n")
    val string = s"$className($detail)"
    string.replace("List(", "Seq(").replace("ArrayBuffer(", "Seq(").replace("ListBuffer(", "Seq(")
  }
}
