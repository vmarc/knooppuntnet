import {List} from "immutable";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlannerContext} from "../context/planner-context";
import {FeatureId} from "../features/feature-id";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveStartPoint implements PlannerCommand {

  constructor(private readonly oldNode: PlanNode,
              private readonly newNode: PlanNode) {
  }

  public do(context: PlannerContext) {
    this.update(context, this.oldNode, this.newNode);
  }

  public undo(context: PlannerContext) {
    this.update(context, this.newNode, this.oldNode);
  }

  public update(context: PlannerContext, fromNode: PlanNode, toNode: PlanNode) {
    context.routeLayer.removeFlag(context.plan.sourceFlag);
    const sourceFlagFeatureId = context.plan.sourceFlag === null ? FeatureId.next() : context.plan.sourceFlag.featureId;
    const newFlag = PlanFlag.start(sourceFlagFeatureId, toNode);
    context.routeLayer.addFlag(newFlag);
    const newPlan = new Plan(toNode, newFlag, List());
    context.updatePlan(newPlan);
  }

}
