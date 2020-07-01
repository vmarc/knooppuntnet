import {PlannerContext} from "../context/planner-context";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveEndPoint implements PlannerCommand {

  constructor(private readonly oldLastLegId: string,
              private readonly newLastLegId: string) {
  }

  public do(context: PlannerContext) {
    this.update(context, this.oldLastLegId, this.newLastLegId);
  }

  public undo(context: PlannerContext) {
    this.update(context, this.newLastLegId, this.oldLastLegId);
  }

  private update(context: PlannerContext, fromLegId: string, toLegId: string) {
    const fromLeg = context.legs.getById(fromLegId);
    const toLeg = context.legs.getById(toLegId);
    context.routeLayer.removeFlag(fromLeg.sinkNode.featureId);
    context.routeLayer.addFlag(PlanFlag.fromEndNode(toLeg.sinkNode));
    context.routeLayer.removeRouteLeg(fromLeg.featureId);
    context.routeLayer.addRouteLeg(toLeg);
    const newLegs = context.plan.legs.update(context.plan.legs.size - 1, () => toLeg);
    const newPlan = PlanUtil.plan(context.plan.sourceNode, newLegs);
    context.updatePlan(newPlan);
  }

}
