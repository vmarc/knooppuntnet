package kpn.core.overpass

class OverpassQueryExecutorMock extends OverpassQueryExecutor {
  override def execute(queryString: String): String = throw new RuntimeException("Unexpected")
}
