import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
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
    context.routeLayer.removeFlagWithFeatureId(fromLeg.sourceNode.featureId);
    context.routeLayer.addFlag(PlanFlag.oldStart(toLeg.sourceNode));
    context.routeLayer.removePlanLeg(fromLeg.featureId);
    context.routeLayer.addPlanLeg(toLeg);
    const newSourceNode = toLeg.sourceNode;
    const newLegs = context.plan.legs.update(0, () => toLeg);
    const newPlan = new Plan(newSourceNode, newLegs);
    context.updatePlan(newPlan);
  }

}
