package kpn.core.db.views

object Common {

  val subsetIndex: String =
    """
      |var calculateSubsetIndex = function(country, networkType) {
      |  if (country == "nl") {
      |    if(networkType == "rwn") {
      |      return 0;
      |    }
      |    if (networkType == "rcn") {
      |      return 1;
      |    }
      |  }
      |  if (country == "be") {
      |    if(networkType == "rwn") {
      |      return 2;
      |    }
      |    if (networkType == "rcn") {
      |      return 3;
      |    }
      |  }
      |  if (country == "de") {
      |    if(networkType == "rwn") {
      |      return 4;
      |    }
      |    if (networkType == "rcn") {
      |      return 5;
      |    }
      |  }
      |  return -1;
      |}
      |
      |var subsetIndex = function(element) {
      |  return calculateSubsetIndex(element.country, element.networkType);
      |}
    """.stripMargin
}
