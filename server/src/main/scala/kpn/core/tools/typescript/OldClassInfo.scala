package kpn.core.tools.typescript

case class OldClassInfo(
  className: String,
  fields: Seq[OldClassField],
  dependencies: Seq[OldClassDependency],
  formClass: Boolean
)
