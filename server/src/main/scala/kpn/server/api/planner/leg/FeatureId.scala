package kpn.server.api.planner.leg

class FeatureId {

  private val min = 10000L
  private val max = 1000000L
  private var id = min

  def next: String = {
    if (id >= max) {
      id = min
    }
    id = id + 1
    id.toString
  }

}
