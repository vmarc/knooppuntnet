package kpn.server.config

import kpn.api.common.data.raw.RawNode
import kpn.api.custom.ApiResponse
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.api.time.Timestamps
import kpn.core.util.UnitTest

import java.io.StringReader
import java.io.StringWriter
import java.lang.reflect.Type

class BsonHttpMessageConverterTest extends UnitTest {

  test("read an write case class") {
    val converter = new BsonHttpMessageConverter()

    val rawNode = newRawNode()

    val json = toJson(converter, rawNode, classOf[RawNode])
    val rawNodeResult = fromJson(converter, json)

    assertEqual(
      rawNode,
      rawNodeResult
    )
  }

  test("write ApiResponse, verify unsupported read") {
    val converter = new BsonHttpMessageConverter()

    val apiResponse = newApiResponse()

    val json = toJson(converter, apiResponse, classOf[ApiResponse[RawNode]])

    assertEqual(
      json,
      """{"situationOn": "2015-08-11T00:00:00Z", "version": 12, "result": {"id": 123, "latitude": "1111", "longitude": "2222", "version": 3, "timestamp": "2025-08-11T12:30:05Z", "changeSetId": 5, "tags": [{"key": "key1", "value": "value1"}, {"key": "key2", "value": "value2"}]}}"""
    )

    intercept[Exception] {
      val reader = new StringReader(json)
      converter.readInternal(classOf[ApiResponse[RawNode]], reader)
    }.getMessage should equal("ApiResponse decoding not supported")
  }

  private def fromJson(converter: BsonHttpMessageConverter, json: String) = {
    val reader = new StringReader(json)
    converter.readInternal(classOf[RawNode], reader)
  }

  private def toJson(converter: BsonHttpMessageConverter, instance: Any, objectType: Type) = {
    val writer = new StringWriter()
    converter.writeInternal(instance, objectType, writer)
    writer.close()
    writer.toString
  }

  private def newApiResponse(): ApiResponse[RawNode] = {
    val rawNode = newRawNode()
    ApiResponse(
      situationOn = Some(Timestamps.default),
      version = 12,
      result = Some(
        rawNode
      )
    )
  }

  private def newRawNode(): RawNode = {
    RawNode(
      id = 123,
      latitude = "1111",
      longitude = "2222",
      version = 3,
      timestamp = Timestamp(2025, 8, 11, 12, 30, 5),
      changeSetId = 5,
      tags = Seq(
        Tag("key1", "value1"),
        Tag("key2", "value2")
      )
    )
  }
}
