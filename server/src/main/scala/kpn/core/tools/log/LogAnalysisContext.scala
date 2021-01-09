package kpn.core.tools.log

case class LogAnalysisContext(
  key: String,
  recordAnalysis: LogRecordAnalysis = LogRecordAnalysis(),
  values: Map[String, Int] = Map.empty
) {

  def withValue(valueKey: String): LogAnalysisContext = {
    val newValues = values.updated(valueKey, values.getOrElse(valueKey, 0) + 1)
    copy(values = newValues)
  }

  def newRecord(): LogAnalysisContext = {
    copy(recordAnalysis = LogRecordAnalysis())
  }

}
