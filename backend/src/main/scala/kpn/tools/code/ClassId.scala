package kpn.tools.code

case class ClassId(
  className: String,
  packageName: String,
) {
  def fullName: String = {
    s"${packageName}.${className}"
  }
}
