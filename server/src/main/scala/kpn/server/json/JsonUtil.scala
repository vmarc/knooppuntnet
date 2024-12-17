package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonMappingException
import enumeratum.EnumEntry

object JsonUtil {
  def deserializationExpection[T <: EnumEntry](jsonParser: JsonParser, value: String, validValues: Seq[T]): JsonMappingException = {
    val validValuesString = validValues.map(enumEntry => s""""${enumEntry.entryName}"""").mkString(", ")
    JsonMappingException.from(
      jsonParser,
      s"""Could not deserialize value "$value", should be one of $validValuesString"""
    )
  }
}
