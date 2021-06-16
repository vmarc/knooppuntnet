package kpn.core.mongo.tools.validation

object ValidationResult {

  val expectedMillisInteractive: Int = 2500

  def validate(name: String)(query: => Option[String]): ValidationResult = {
    validateMillis(name, expectedMillisInteractive)(query)
  }

  def validateMillis(name: String, expectedMillis: Int)(query: => Option[String]): ValidationResult = {
    val t1 = System.nanoTime()
    val message = query
    val t2 = System.nanoTime()
    val elapsedMillis = (t2 - t1) / 1000000
    ValidationResult(name, message, expectedMillis, elapsedMillis.toInt)
  }
}

case class ValidationResult(
  name: String,
  message: Option[String],
  expectedMillis: Int,
  elapsedMillis: Int
) {

  def isOk: Boolean = {
    message.isEmpty && elapsedMillis < expectedMillis
  }
}