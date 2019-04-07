import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerContext} from "../interaction/planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveEndPoint implements PlannerCommand {

  constructor(private readonly oldLastLeg: PlanLeg,
              private readonly newLastLeg: PlanLeg) {
  }

  public do(context: PlannerContext) {
    this.update(context, this.oldLastLeg, this.newLastLeg);
  }

  public undo(context: PlannerContext) {
    this.update(context, this.newLastLeg, this.oldLastLeg);
  }

  private update(context: PlannerContext, fromLeg: PlanLeg, toLeg: PlanLeg) {
    context.removeViaNodeFlag(fromLeg.legId, fromLeg.sink.nodeId);
    context.addViaNodeFlag(toLeg.legId, toLeg.sink.nodeId, toLeg.sink.coordinate);
    context.removeRouteLeg(fromLeg.legId);
    context.addRouteLeg(toLeg.legId);
    const plan = context.plan();
    const newLegs = plan.legs.update(plan.legs.size - 1, () => toLeg);
    const newPlan = new Plan(plan.source, newLegs);
    context.updatePlan(newPlan);
  }

}
