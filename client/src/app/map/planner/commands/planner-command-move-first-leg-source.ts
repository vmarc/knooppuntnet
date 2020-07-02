import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlannerContext} from "../context/planner-context";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveFirstLegSource implements PlannerCommand {

  constructor(private readonly oldFirstLegId: string,
              private readonly newFirstLegId: string) {
  }

  public do(context: PlannerContext) {
    this.update(context, this.oldFirstLegId, this.newFirstLegId);
  }

  public undo(context: PlannerContext) {
    this.update(context, this.newFirstLegId, this.oldFirstLegId);
  }

  public update(context: PlannerContext, fromLegId: string, toLegId: string) {
    const fromLeg = context.legs.getById(fromLegId);
    const toLeg = context.legs.getById(toLegId);
    context.routeLayer.removeFlag(fromLeg.sourceNode.featureId);
    context.routeLayer.addFlag(PlanFlag.fromStartNode(toLeg.sourceNode));
    context.routeLayer.removePlanLeg(fromLeg.featureId);
    context.routeLayer.addPlanLeg(toLeg);
    const newSourceNode = toLeg.sourceNode;
    const newLegs = context.plan.legs.update(0, () => toLeg);
    const newPlan = new Plan(newSourceNode, newLegs);
    context.updatePlan(newPlan);
  }

}
