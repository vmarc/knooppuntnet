import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
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
    context.routeLayer.removeViaNodeFlag(fromLeg.legId, fromLeg.sink.nodeId);
    context.routeLayer.addViaNodeFlag(toLeg.legId, toLeg.sink.nodeId, toLeg.sink.coordinate);
    context.routeLayer.removeRouteLeg(fromLeg.legId);
    context.routeLayer.addRouteLeg(toLeg);
    const plan = context.plan();
    const newLegs = plan.legs.update(plan.legs.size - 1, () => toLeg);
    const newPlan = new Plan(plan.source, newLegs);
    context.updatePlan(newPlan);
  }

}
