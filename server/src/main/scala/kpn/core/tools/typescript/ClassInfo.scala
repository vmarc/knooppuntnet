package kpn.core.tools.typescript

case class ClassInfo(
  className: String,
  fields: Seq[ClassField],
  dependencies: Seq[ClassDependency],
  formClass: Boolean
)
