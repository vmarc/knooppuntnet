package kpn.tools.code.domain

import kpn.tools.code.ClassId

case class ClassInfo(
  className: String,
  packageName: String,
  fields: Seq[ClassField],
  isEnum: Boolean = false,
) {
  def dependencies: Seq[ClassId] = {
    fields
      .flatMap(field => Seq(field.classType) ++ field.classType.arrayType.toSeq)
      .filter(classType => classType.packageName.isDefined && classType.typeName.isDefined)
      .map(classType => ClassId(classType.typeName.get, classType.packageName.get))
      .distinct
  }

  def key: String = {
    s"${packageName}.${className}"
  }
}
