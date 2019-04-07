import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerContext} from "../interaction/planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveStartPoint implements PlannerCommand {

  constructor(private readonly oldFirstLeg: PlanLeg,
              private readonly newFirstLeg: PlanLeg) {
  }

  public do(context: PlannerContext) {
    this.update(context, this.oldFirstLeg, this.newFirstLeg);
  }

  public undo(context: PlannerContext) {
    this.update(context, this.newFirstLeg, this.oldFirstLeg);
  }

  public update(context: PlannerContext, fromLeg: PlanLeg, toLeg: PlanLeg) {
    context.removeStartNodeFlag(fromLeg.source.nodeId);
    context.addStartNodeFlag(toLeg.source.nodeId, toLeg.source.coordinate);
    context.removeRouteLeg(fromLeg.legId);
    context.addRouteLeg(toLeg.legId);
    const newSource = toLeg.source;
    const newLegs = context.plan().legs.update(0, () => toLeg);
    const newPlan = new Plan(newSource, newLegs);
    context.updatePlan(newPlan);
  }

}
