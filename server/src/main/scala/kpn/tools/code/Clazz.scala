package kpn.tools.code

import scala.reflect.runtime.universe.Type

class Clazz(private val typeSignature: Type) {

  val fullName: String = typeSignature.typeSymbol.fullName
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
    typeSignature.baseClasses.exists(_.fullName == "kpn.core.doc.Storable")
  }

  def isApi: Boolean = {
    packageName.startsWith("kpn.api")
  }
}
