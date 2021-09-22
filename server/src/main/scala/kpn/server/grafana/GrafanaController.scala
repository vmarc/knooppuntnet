package kpn.server.grafana

import kpn.server.json.Json
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GrafanaController {

  @GetMapping(value = Array("/grafana"))
  def healthCheck(): String = {
    "OK"
  }

  @PostMapping(value = Array("/grafana/search"))
  def search(@RequestBody parameters: String): Seq[String] = {
    println(s"search: $parameters")
    Seq("aaa", "bbb", "ccc")
  }

  @PostMapping(value = Array("/grafana/query"))
  def query(@RequestBody parameters: String): String = {
    println(s"query: $parameters")

    val query = Json.objectMapper.readValue(parameters, classOf[GrafanaQuery])

    def time(intervals: Long): Long = {
      query.intervalMs * intervals + query.startTime - query.intervalMs * query.maxDataPoints
    }

    val timeseries1 = GrafanaTimeSeries(
     target = "een", // query.targets.head.target,
     datapoints = Seq(
        0f -> time(0),
        50f -> time(100),
        100f -> time(200),
        30f -> time(300),
        200f -> time(400),
        150f -> time(500),
        50f -> time(600)
      )
    )

    val timeseries2 = GrafanaTimeSeries(
      target = "twee", // query.targets.head.target,
      datapoints = Seq(
        0f -> time(0),
        250f -> time(100),
        180f -> time(200),
        170f -> time(300),
        120f -> time(400),
        50f -> time(500),
        40f -> time(600)
      )
    )

    val timeseries3 = GrafanaTimeSeries(
      target = "gemiddeld", // query.targets.head.target,
      datapoints = Seq(
        150f -> time(0),
        150f -> time(query.maxDataPoints)
      )
    )

    val response = Seq(timeseries1, timeseries2, timeseries3)
    val responseString = Json.objectMapper.writeValueAsString(response)
    println("Query response: " + responseString)
    responseString
  }

  @PostMapping(value = Array("/grafana/tag-keys"))
  def tagKeys(@RequestBody parameters: String): Seq[String] = {
    println(s"tag-keys: $parameters")
    Seq()
  }

  @PostMapping(value = Array("/grafana/tag-values"))
  def tagValues(@RequestBody parameters: String): Seq[String] = {
    println(s"tag-values: $parameters")
    Seq()
  }
}
