package kpn.tools.code.domain

case class ClassField(
  name: String,
  classType: ClassType
) {
  def typeString: String = {
    classType.arrayType match {
      case Some(arrayType) => s"Seq[${arrayType.typeName.getOrElse("unknown")}]"
      case None => classType.typeName.getOrElse("unknown")
    }
  }
}
