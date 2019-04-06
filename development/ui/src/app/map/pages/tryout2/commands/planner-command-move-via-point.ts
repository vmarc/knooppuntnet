import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerContext} from "../planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveViaPoint implements PlannerCommand {

  constructor(private readonly startNodeFeatureId: string,
              private readonly indexleg1: number,
              private readonly oldLeg1: PlanLeg,
              private readonly oldLeg2: PlanLeg,
              private readonly newLeg1: PlanLeg,
              private readonly newLeg2: PlanLeg) {
  }

  public do(context: PlannerContext) {
    this.update(context, this.oldLeg1, this.oldLeg2, this.newLeg1, this.newLeg2);
  }

  public undo(context: PlannerContext) {
    this.update(context, this.newLeg1, this.newLeg2, this.oldLeg1, this.oldLeg2);
  }

  private update(context: PlannerContext, fromLeg1: PlanLeg, fromLeg2: PlanLeg, toLeg1: PlanLeg, toLeg2: PlanLeg) {
    context.updateFlagPosition(this.startNodeFeatureId, toLeg2.source.coordinate);
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
