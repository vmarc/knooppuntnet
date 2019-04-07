import {PlannerContext} from "../interaction/planner-context";
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

    const fromLeg1 = context.legCache().getById(fromLegId1);
    const fromLeg2 = context.legCache().getById(fromLegId2);
    const toLeg1 = context.legCache().getById(toLegId1);
    const toLeg2 = context.legCache().getById(toLegId2);

    context.removeViaNodeFlag(fromLeg1.legId, fromLeg1.sink.nodeId);
    context.addViaNodeFlag(toLeg1.legId, toLeg1.sink.nodeId, toLeg1.sink.coordinate);
    context.removeRouteLeg(fromLeg1.legId);
    context.removeRouteLeg(fromLeg2.legId);
    context.addRouteLeg(toLeg1.legId);
    context.addRouteLeg(toLeg2.legId);

    const plan = context.plan();
    const newLegs1 = plan.legs.update(this.indexleg1, () => toLeg1);
    const newLegs2 = newLegs1.update(this.indexleg1 + 1, () => toLeg2);
    const newPlan = new Plan(plan.source, newLegs2);
    context.updatePlan(newPlan);
  }

}
