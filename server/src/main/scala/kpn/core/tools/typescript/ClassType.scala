package kpn.core.tools.typescript

case class ClassType(
  typeName: String,
  primitive: Boolean = false,
  arrayType: Option[ClassType] = None,
  mapTypes: Option[(ClassType, ClassType)] = None,
  optional: Boolean = false
) {

  def isArray: Boolean = arrayType.isDefined

  def isMap: Boolean = mapTypes.isDefined

}
