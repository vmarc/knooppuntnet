package kpn.core.tools.typescript

case class OldClassType(
  typeName: String,
  primitive: Boolean = false,
  arrayType: Option[OldClassType] = None,
  mapTypes: Option[(OldClassType, OldClassType)] = None,
  optional: Boolean = false
) {

  def isArray: Boolean = arrayType.isDefined

  def isMap: Boolean = mapTypes.isDefined
}
