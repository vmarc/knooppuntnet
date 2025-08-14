package kpn.tools.code

import kpn.tools.code.domain.ClassField
import kpn.tools.code.domain.ClassType

import scala.reflect.runtime.universe.MethodSymbol
import scala.reflect.runtime.universe.Type
import scala.util.matching.Regex

object Clazz {
  private val mapSignature: Regex = """Map\[([a-zA-Z0-9.]*),([a-zA-Z0-9.]*)\]""".r
  private val setSignature: Regex = """Set\[([a-zA-Z0-9.]*)\]""".r
  private val seqSignature: Regex = """Seq\[([a-zA-Z0-9.]*)\]""".r
  private val arraySignature: Regex = """Array\[([a-zA-Z0-9.]*)\]""".r
  private val vectorSignature: Regex = """Vector\[([a-zA-Z0-9.]*)\]""".r
  private val optionSignature: Regex = """^Option\[(.*)\]$""".r
}

class Clazz(private val typeSignature: Type) {

  val fullName: String = {
    val name = typeSignature.typeSymbol.fullName
    if (name.endsWith("$")) {
      name.dropRight(1)
    }
    else {
      name
    }
  }
  val className: String = fullName.split("\\.").last
  val packageName: String = fullName.dropRight(className.length + 1)

  def enumClassName: String = {
    className.split("\\$").head
  }

  def isEnum: Boolean = {
    typeSignature.baseClasses.exists(_.fullName == "enumeratum.Enum")
  }

  def isEnumEntry: Boolean = {
    typeSignature.baseClasses.exists(_.fullName == "enumeratum.EnumEntry")
  }

  def isStorable: Boolean = {
    typeSignature.baseClasses.exists(_.fullName == "kpn.core.doc.Storable") && isCaseClass
  }

  def isApi: Boolean = {
    packageName.startsWith("kpn.api") && isCaseClass
  }

  def isCaseClass: Boolean = {
    typeSignature.decls.exists(isCaseAccessorMethod)
  }

  def fields: Seq[ClassField] = {
    typeSignature.decls.toSeq.flatMap {
      case m: MethodSymbol if m.isCaseAccessor =>
        val fieldName = m.name.toString
        val fieldTypeString = m.typeSignature.toString
        val classType = buildClassType(fieldTypeString)
        Some(ClassField(fieldName, classType))
      case _ =>
        None
    }
  }

  @scala.annotation.tailrec
  private def buildClassType(fieldTypeString: String, optional: Boolean = false): ClassType = {

    fieldTypeString match {

      case Clazz.mapSignature(type1, type2) =>
        val typeName1 = fieldTypeToTypescript(type1, optional)
        val typeName2 = fieldTypeToTypescript(type2, optional)
        ClassType(
          mapTypes = Some((typeName1, typeName2)),
          optional = optional
        )

      case Clazz.setSignature(type1) =>
        val typeName1 = fieldTypeToTypescript(type1, optional)
        ClassType(
          arrayType = Some(typeName1),
          arrayTypeClass = Some("Set"),
          optional = optional
        )

      case Clazz.seqSignature(type1) =>
        val typeName1 = fieldTypeToTypescript(type1, optional)
        ClassType(
          arrayType = Some(typeName1),
          arrayTypeClass = Some("Seq"),
          optional = optional
        )

      case Clazz.vectorSignature(type1) =>
        val typeName1 = fieldTypeToTypescript(type1, optional)
        ClassType(
          arrayType = Some(typeName1),
          arrayTypeClass = Some("Vector"),
          optional = optional
        )

      case Clazz.arraySignature(type1) =>
        val typeName1 = fieldTypeToTypescript(type1, optional)
        ClassType(
          arrayType = Some(typeName1),
          arrayTypeClass = Some("Array"),
          optional = optional
        )

      case Clazz.optionSignature(type1) =>
        buildClassType(type1, optional = true)

      case _ =>
        fieldTypeToTypescript(fieldTypeString, optional)
    }
  }

  private def fieldTypeToTypescript(fieldTypeName: String, optional: Boolean): ClassType = {
    fieldTypeName match {
      case "Int" => ClassType(Some(fieldTypeName), primitive = true, optional = optional)
      case "Long" => ClassType(Some(fieldTypeName), primitive = true, optional = optional)
      case "Double" => ClassType(Some(fieldTypeName), primitive = true, optional = optional)
      case "String" => ClassType(Some(fieldTypeName), primitive = true, optional = optional)
      case "Boolean" => ClassType(Some(fieldTypeName), primitive = true, optional = optional)
      case _ =>
        val className = fieldTypeName.split("\\.").last
        val packageName = fieldTypeName.dropRight(className.length + 1)
        ClassType(Some(className), Some(packageName), optional = optional)
    }
  }

  private def isCaseAccessorMethod(decl: scala.reflect.runtime.universe.Symbol): Boolean = {
    decl match {
      case m: MethodSymbol if m.isCaseAccessor => true
      case _ => false
    }
  }
}
