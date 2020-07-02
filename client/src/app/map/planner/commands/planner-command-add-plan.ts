import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlannerContext} from "../context/planner-context";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddPlan implements PlannerCommand {

  constructor(private plan: Plan) {
  }

  public do(context: PlannerContext) {
    context.routeLayer.addPlan(this.plan);
    context.updatePlan(this.plan);
  }

  public undo(context: PlannerContext) {
    context.routeLayer.removePlan(this.plan);
    context.updatePlan(PlanUtil.emptyPlan);
  }

}
