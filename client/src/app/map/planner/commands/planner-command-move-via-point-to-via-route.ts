import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {ViaRoute} from "../../../kpn/api/common/planner/via-route";
import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveViaPointToViaRoute implements PlannerCommand {

  constructor(private readonly oldLegId1: string,
              private readonly oldLegId2: string,
              private readonly newLegId: string,
              private readonly viaRoute: ViaRoute,
              private readonly coordinate: Coordinate) {
  }

  public do(context: PlannerContext) {
    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg = context.legs.getById(this.newLegId);

    context.routeLayer.removeFlag(oldLeg1.sink.featureId);
    context.routeLayer.addFlag(new PlanFlag(PlanFlagType.Via, this.flagFeatureId(), this.coordinate));
    context.routeLayer.removeRouteLeg(oldLeg1.featureId);
    context.routeLayer.removeRouteLeg(oldLeg2.featureId);
    context.routeLayer.addRouteLeg(newLeg);

    const newLegs: List<PlanLeg> = context.plan.legs
      .map(leg => leg.featureId === oldLeg1.featureId ? newLeg : leg)
      .filter(leg => leg.featureId !== oldLeg2.featureId);
    const newPlan = Plan.create(context.plan.source, newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg = context.legs.getById(this.newLegId);

    context.routeLayer.removeFlag(this.flagFeatureId());
    context.routeLayer.addFlag(PlanFlag.fromViaNode(oldLeg1.sink));
    context.routeLayer.addRouteLeg(oldLeg1);
    context.routeLayer.addRouteLeg(oldLeg2);
    context.routeLayer.removeRouteLeg(newLeg.featureId);

    const legIndex = context.plan.legs.findIndex(leg => leg.featureId === newLeg.featureId);
    if (legIndex > -1) {
      const newLegs1 = context.plan.legs.update(legIndex, () => oldLeg1);
      const newLegs2 = newLegs1.insert(legIndex + 1, oldLeg2);
      const newPlan = Plan.create(context.plan.source, newLegs2);
      context.updatePlan(newPlan);
    }
  }

  private flagFeatureId(): string {
    return this.viaRoute.routeId + "-" + this.viaRoute.pathId;
  }

}
