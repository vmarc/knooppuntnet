package kpn.tools.code.domain

case class ClassField(
  name: String,
  classType: ClassType
) {
  def typeString: String = {
    if (classType.optional) {
      s"Option[$typeStringDetail]"
    }
    else {
      typeStringDetail
    }
  }

  private def typeStringDetail: String = {
    classType.arrayType match {
      case Some(arrayType) =>
        val arrayTypeClass = classType.arrayTypeClass.get
        val typeName = arrayType.typeName.getOrElse(throw new IllegalStateException("typeName for array elements is missing"))
        s"$arrayTypeClass[$typeName]"
      case None =>
        classType.mapTypes match {
          case Some((keyType, valueType)) =>
            val keyTypeName = keyType.typeName.getOrElse(throw new IllegalStateException("key typeName is missing"))
            val valueTypeName = valueType.typeName.getOrElse(throw new IllegalStateException("value typeName is missing"))
            s"Map[$keyTypeName, $valueTypeName]"
          case None =>
            classType.typeName.getOrElse(throw new IllegalStateException("typeName is missing"))
        }
    }
  }
}
