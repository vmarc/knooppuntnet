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
      case Some(arrayType) => s"${classType.arrayTypeClass.get}[${arrayType.typeName.getOrElse("unknown")}]"
      case None => classType.typeName.getOrElse("unknown")
    }
  }
}
