import {List} from "immutable";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddStartPoint implements PlannerCommand {

  constructor(private featureId: string, private node: PlanNode) {
  }

  public do(context: PlannerContext) {
    const plan = new Plan(this.node, List());
    context.routeLayer.addFlag(PlanFlag.start(this.featureId, this.node));
    context.updatePlan(plan);
  }

  public undo(context: PlannerContext) {
    context.updatePlan(Plan.empty);
    context.routeLayer.removeFlagWithFeatureId(this.featureId);
  }

}
