import {Coordinate} from "ol/coordinate";
import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {ViaRoute} from "../plan/via-route";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveViaPointToViaRoute implements PlannerCommand {

  constructor(private readonly indexleg1: number,
              private readonly oldLegId1: string,
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
    context.routeLayer.addFlag(new PlanFlag(PlanFlagType.Via, this.viaRoute.routeId + "-" + this.viaRoute.pathId, this.coordinate));
    context.routeLayer.removeRouteLeg(oldLeg1.featureId);
    context.routeLayer.removeRouteLeg(oldLeg2.featureId);
    context.routeLayer.addRouteLeg(newLeg);

    const newLegs1 = context.plan.legs.update(this.indexleg1, () => newLeg);
    const newLegs2 = newLegs1.remove(this.indexleg1 + 1);
    const newPlan = Plan.create(context.plan.source, newLegs2);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {
    // TODO PLANNER
  }
}
