package kpn.tools.code.domain

case class ClassInfo(
  className: String,
  packageName: String,
  fields: Seq[ClassField],
)
