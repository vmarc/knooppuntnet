import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveViaPoint implements PlannerCommand {

  constructor(private readonly startNodeFeatureId: string,
              private readonly indexleg1: number,
              private readonly oldLegId1: string,
              private readonly oldLegId2: string,
              private readonly newLegId1: string,
              private readonly newLegId2: string) {
  }

  public do(context: PlannerContext) {
    this.update(context, this.oldLegId1, this.oldLegId2, this.newLegId1, this.newLegId2);
  }

  public undo(context: PlannerContext) {
    this.update(context, this.newLegId1, this.newLegId2, this.oldLegId1, this.oldLegId2);
  }

  private update(context: PlannerContext, fromLegId1: string, fromLegId2: string, toLegId1: string, toLegId2: string) {

    const fromLeg1 = context.legs.getById(fromLegId1);
    const fromLeg2 = context.legs.getById(fromLegId2);
    const toLeg1 = context.legs.getById(toLegId1);
    const toLeg2 = context.legs.getById(toLegId2);

    context.routeLayer.removeViaNodeFlag(fromLeg1.legId, fromLeg1.sink.nodeId);
    context.routeLayer.addViaNodeFlag(toLeg1.legId, toLeg1.sink.nodeId, toLeg1.sink.coordinate);
    context.routeLayer.removeRouteLeg(fromLeg1.legId);
    context.routeLayer.removeRouteLeg(fromLeg2.legId);
    context.routeLayer.addRouteLeg(toLeg1);
    context.routeLayer.addRouteLeg(toLeg2);

    const newLegs1 = context.plan.legs.update(this.indexleg1, () => toLeg1);
    const newLegs2 = newLegs1.update(this.indexleg1 + 1, () => toLeg2);
    const newPlan = new Plan(context.plan.source, newLegs2);
    context.updatePlan(newPlan);
  }

}
