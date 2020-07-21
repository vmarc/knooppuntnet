import {PlannerContext} from "../context/planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveEndPoint implements PlannerCommand {

  constructor(private readonly oldLastLegId: string,
              private readonly newLastLegId: string) {
  }

  public do(context: PlannerContext) {
    context.debug("PlannerCommandMoveEndPoint");
    this.update(context, this.oldLastLegId, this.newLastLegId);
  }

  public undo(context: PlannerContext) {
    context.debug("PlannerCommandMoveEndPoint undo");
    this.update(context, this.newLastLegId, this.oldLastLegId);
  }

  private update(context: PlannerContext, fromLegId: string, toLegId: string) {
    const fromLeg = context.legs.getById(fromLegId);
    const toLeg = context.legs.getById(toLegId);
    context.markerLayer.removeFlag(fromLeg.viaFlag);
    context.markerLayer.removeFlag(fromLeg.sinkFlag);
    context.routeLayer.removePlanLeg(fromLeg.featureId);
    context.markerLayer.addFlag(toLeg.viaFlag);
    context.markerLayer.addFlag(toLeg.sinkFlag);
    context.routeLayer.addPlanLeg(toLeg);
    const newLegs = context.plan.legs.map(leg => leg.featureId === fromLeg.featureId ? toLeg : leg);
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

}
