package kpn.core.tools.typescript

case class ClassInfo(
  className: String,
  fileName: String,
  fields: Seq[ClassField],
  dependencies: Seq[ClassDependency]
) {

  def displayString: String = {

    val fieldsString = fields.map { field =>
      s"    name=${field.name} typeName=${field.classType.typeName} array=${field.classType.isArray}"
    }.mkString("", "\n", "\n")

    val dependenciesString = dependencies.map { dependency =>
      s"    className=${dependency.className} fileName=${dependency.fileName}"
    }.mkString("", "\n", "\n")

    Seq(
      className,
      s"  fileName=$fileName",
      s"  fields",
      fieldsString,
      s"  dependencies",
      dependenciesString
    ).mkString("\n")
  }
}
