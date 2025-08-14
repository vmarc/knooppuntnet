package kpn.tools.code.domain

case class ClassType(
  typeName: Option[String] = None,
  packageName: Option[String] = None,
  primitive: Boolean = false, // TODO code generation - not used???
  arrayType: Option[ClassType] = None,
  arrayTypeClass: Option[String] = None,
  mapTypes: Option[(ClassType, ClassType)] = None,
  optional: Boolean = false
) {

  def isArray: Boolean = arrayType.isDefined

  def isMap: Boolean = mapTypes.isDefined
}
