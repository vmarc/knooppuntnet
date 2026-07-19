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

  def collectionDependencies: Seq[ClassId] = {
    fields.flatMap(field => field.classType.arrayTypeClass.toSeq).distinct.map {
      case "Seq" => ClassId("ImmutableList", "com.google.common.collect")
      case "Set" => ClassId("ImmutableSet", "com.google.common.collect")
      case "Vector" => ClassId("ImmutableList", "com.google.common.collect")
      case classType =>
        throw new IllegalArgumentException(s"Unknown collection type: $classType")
    }
  }

  def javaUtilDependencies: Seq[ClassId] = {
    if (fields.exists(_.classType.optional)) {
      Seq(ClassId("Optional", "java.util"))
    }
    else {
      Seq.empty
    }
  }

  def fullName: String = {
    s"$packageName.$className"
  }
}
