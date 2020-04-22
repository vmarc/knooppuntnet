package kpn.server.repository

import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.util.UnitTest

class QueryParametersTest extends UnitTest {

  test("changes all") {

    val parameters = ChangesParameters()

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["change-set", "9999"]""")
    queryParameters("endkey") should equal("""["change-set"]""")
  }

  test("year") {

    val parameters = ChangesParameters(year = Some("2017"))

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["change-set", "2018"]""")
    queryParameters("endkey") should equal("""["change-set", "2017"]""")
  }

  test("month") {

    val parameters = ChangesParameters(year = Some("2017"), month = Some("08"))

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["change-set", "2017", "09"]""")
    queryParameters("endkey") should equal("""["change-set", "2017", "08"]""")
  }

  test("month at end of year") {

    val parameters = ChangesParameters(year = Some("2017"), month = Some("12"))

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["change-set", "2018", "01"]""")
    queryParameters("endkey") should equal("""["change-set", "2017", "12"]""")
  }

  test("day") {

    val parameters = ChangesParameters(year = Some("2017"), month = Some("08"), day = Some("11"))

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["change-set", "2017", "08", "12"]""")
    queryParameters("endkey") should equal("""["change-set", "2017", "08", "11"]""")
  }

  test("day at end of month") {

    val parameters = ChangesParameters(year = Some("2017"), month = Some("01"), day = Some("31"))

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["change-set", "2017", "02", "01"]""")
    queryParameters("endkey") should equal("""["change-set", "2017", "01", "31"]""")
  }

  test("day at end of year") {

    val parameters = ChangesParameters(year = Some("2017"), month = Some("12"), day = Some("31"))

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["change-set", "2018", "01", "01"]""")
    queryParameters("endkey") should equal("""["change-set", "2017", "12", "31"]""")
  }

  test("default items per page - page 1") {
    val parameters = ChangesParameters(pageIndex = 0)
    QueryParameters.from(parameters)("skip") should equal("0")
  }

  test("default items per page - page 2") {
    val parameters = ChangesParameters(pageIndex = 1)
    QueryParameters.from(parameters)("skip") should equal("5")
  }

  test("default items per page - page 3") {
    val parameters = ChangesParameters(pageIndex = 2)
    QueryParameters.from(parameters)("skip") should equal("10")
  }

  test("10 items per page - page 1") {
    val parameters = ChangesParameters(itemsPerPage = 10, pageIndex = 0)
    QueryParameters.from(parameters)("skip") should equal("0")
  }

  test("10 items per page - page 2") {
    val parameters = ChangesParameters(itemsPerPage = 10, pageIndex = 1)
    QueryParameters.from(parameters)("skip") should equal("10")
  }

  test("10 items per page - page 3") {
    val parameters = ChangesParameters(itemsPerPage = 10, pageIndex = 2)
    QueryParameters.from(parameters)("skip") should equal("20")
  }

  test("network all") {

    val parameters = ChangesParameters(networkId = Some(123L))

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["network", "123", "9999"]""")
    queryParameters("endkey") should equal("""["network", "123"]""")
  }

  test("network impacted") {

    val parameters = ChangesParameters(networkId = Some(123L), impact = true)

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["impacted:network", "123", "9999"]""")
    queryParameters("endkey") should equal("""["impacted:network", "123"]""")
  }

  test("route all") {

    val parameters = ChangesParameters(routeId = Some(123L))

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["route", "123", "9999"]""")
    queryParameters("endkey") should equal("""["route", "123"]""")
  }

  test("route impacted") {

    val parameters = ChangesParameters(routeId = Some(123L), impact = true)

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["impacted:route", "123", "9999"]""")
    queryParameters("endkey") should equal("""["impacted:route", "123"]""")
  }

  test("node all") {

    val parameters = ChangesParameters(nodeId = Some(123L))

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["node", "123", "9999"]""")
    queryParameters("endkey") should equal("""["node", "123"]""")
  }

  test("node impacted") {

    val parameters = ChangesParameters(nodeId = Some(123L), impact = true)

    val queryParameters = QueryParameters.from(parameters)
    queryParameters("startkey") should equal("""["impacted:node", "123", "9999"]""")
    queryParameters("endkey") should equal("""["impacted:node", "123"]""")
  }
}
