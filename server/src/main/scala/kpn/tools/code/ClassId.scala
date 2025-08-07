package kpn.tools.code

case class ClassId(
  className: String,
  packageName: String,
) {
  def key: String = {
    s"${packageName}.${className}"
  }
}
