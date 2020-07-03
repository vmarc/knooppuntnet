import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerContext} from "../context/planner-context";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveViaPointToViaRoute implements PlannerCommand {

  constructor(private readonly oldLegId1: string,
              private readonly oldLegId2: string,
              private readonly newLegId: string,
              // private readonly viaRoute: ViaRoute,
              private readonly coordinate: Coordinate) {
  }

  public do(context: PlannerContext) {
    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg = context.legs.getById(this.newLegId);

    context.routeLayer.removeFlagWithFeatureId(oldLeg1.sinkNode.featureId);
    context.routeLayer.addFlag(new PlanFlag(PlanFlagType.Via, this.flagFeatureId(), this.coordinate));
    context.routeLayer.removePlanLeg(oldLeg1.featureId);
    context.routeLayer.removePlanLeg(oldLeg2.featureId);
    context.routeLayer.addPlanLeg(newLeg);

    const newLegs: List<PlanLeg> = context.plan.legs
      .map(leg => leg.featureId === oldLeg1.featureId ? newLeg : leg)
      .filter(leg => leg.featureId !== oldLeg2.featureId);
    const newPlan = new Plan(context.plan.sourceNode, newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg = context.legs.getById(this.newLegId);

    context.routeLayer.removeFlagWithFeatureId(this.flagFeatureId());
    context.routeLayer.addFlag(PlanFlag.oldVia(oldLeg1.sinkNode));
    context.routeLayer.addPlanLeg(oldLeg1);
    context.routeLayer.addPlanLeg(oldLeg2);
    context.routeLayer.removePlanLeg(newLeg.featureId);

    const legIndex = context.plan.legs.findIndex(leg => leg.featureId === newLeg.featureId);
    if (legIndex > -1) {
      const newLegs1 = context.plan.legs.update(legIndex, () => oldLeg1);
      const newLegs2 = newLegs1.insert(legIndex + 1, oldLeg2);
      const newPlan = new Plan(context.plan.sourceNode, newLegs2);
      context.updatePlan(newPlan);
    }
  }

  private flagFeatureId(): string {
    return ""; // this.viaRoute.routeId + "-" + this.viaRoute.pathId;
  }

}
