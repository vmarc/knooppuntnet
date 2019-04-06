import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerContext} from "../planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveEndPoint implements PlannerCommand {

  constructor(private readonly endNodeFeatureId: string,
              private readonly oldLastLeg: PlanLeg,
              private readonly newLastLeg: PlanLeg) {
  }

  public do(context: PlannerContext) {
    context.updateFlagPosition(this.endNodeFeatureId, this.newLastLeg.sink.coordinate);
    context.removeRouteLeg(this.oldLastLeg.legId);
    context.addRouteLeg(this.newLastLeg.legId);
    this.updateLastLeg(context, this.newLastLeg);
  }

  public undo(context: PlannerContext) {
    context.updateFlagPosition(this.endNodeFeatureId, this.oldLastLeg.sink.coordinate);
    context.removeRouteLeg(this.newLastLeg.legId);
    context.addRouteLeg(this.oldLastLeg.legId);
    this.updateLastLeg(context, this.oldLastLeg);
  }

  private updateLastLeg(context: PlannerContext, leg: PlanLeg): void {
    const plan = context.plan();
    const newLegs = plan.legs.update(plan.legs.size - 1, () => leg);
    const newPlan = new Plan(plan.source, newLegs);
    context.updatePlan(newPlan);
  }

}
