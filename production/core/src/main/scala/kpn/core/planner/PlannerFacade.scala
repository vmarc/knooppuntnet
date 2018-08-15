package kpn.core.planner

import kpn.shared.NetworkType

class PlannerFacade {

  def add(networkType: NetworkType, encodedPlanString: String, nodeId: Long): Plan = {
    Plan(Seq(), "")
  }

  def undo(networkType: NetworkType, encodedPlanString: String): Plan = {
    Plan(Seq(), "")
  }

  def reset(networkType: NetworkType, encodedPlanString: String): Plan = {
    Plan(Seq(), "")
  }

}
