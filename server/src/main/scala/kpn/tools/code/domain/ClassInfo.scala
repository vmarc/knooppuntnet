package kpn.tools.code.domain

import kpn.tools.code.ClassId

case class ClassInfo(
  className: String,
  packageName: String,
  fields: Seq[ClassField] = Seq.empty,
  enumClassName: Option[String] = None,
  isEnum: Boolean = false,
  isEnumEntry: Boolean = false,
  enumValues: Seq[String] = Seq.empty
) {

  def enumValue: Option[String] = {
    val value = className.split("\\$").last
    if (enumClassName.contains(value)) {
      None
    }
    else {
      Some(value)
    }
  }

  def dependencies: Seq[ClassId] = {
    fields
      .flatMap(field => Seq(field.classType) ++ field.classType.arrayType.toSeq)
      .filter(classType => classType.packageName.isDefined && classType.typeName.isDefined)
      .map(classType => ClassId(classType.typeName.get, classType.packageName.get))
      .distinct
  }

  def fullName: String = {
    s"$packageName.$className"
  }
}
