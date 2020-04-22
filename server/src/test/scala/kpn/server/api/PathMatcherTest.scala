package kpn.server.api

import kpn.core.util.UnitTest
import org.springframework.util.AntPathMatcher
import org.springframework.util.PathMatcher

class PathMatcherTest extends UnitTest {

  test("subset") {

    val pattern = "/json-api/{country:be|de|fr|nl|at}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/networks"
    val path = "/json-api/be/cycling/networks"

    val matcher: PathMatcher = new AntPathMatcher()
    val result = matcher.extractUriTemplateVariables(pattern, path)

    result.get("country") should equal("be")
    result.get("networkType") should equal("cycling")
  }

}

