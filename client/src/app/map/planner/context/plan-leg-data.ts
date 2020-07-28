import {List} from "immutable";
import {PlanRoute} from "../../../kpn/api/common/planner/plan-route";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";

export class PlanLegData {

  constructor(readonly source: LegEnd,
              readonly sink: LegEnd,
              readonly routes: List<PlanRoute>) {
  }

  get sinkNode(): PlanNode {
    const lastRoute = this.routes.last(null);
    if (lastRoute) {
      return lastRoute.sinkNode;
    }
    return null;
  }

}
