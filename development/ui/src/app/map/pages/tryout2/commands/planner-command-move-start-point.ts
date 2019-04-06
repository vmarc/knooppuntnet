import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerContext} from "../planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveStartPoint implements PlannerCommand {

  constructor(private readonly startNodeFeatureId: string,
              private readonly oldFirstLeg: PlanLeg,
              private readonly newFirstLeg: PlanLeg) {
  }

  public do(context: PlannerContext) {
    context.removeStartNodeFlag(this.oldFirstLeg.source.nodeId);
    context.addStartNodeFlag(this.newFirstLeg.source.nodeId, this.newFirstLeg.source.coordinate);
    context.removeRouteLeg(this.oldFirstLeg.legId);
    context.addRouteLeg(this.newFirstLeg.legId);
    const newSource = this.newFirstLeg.source;
    const newLegs = context.plan().legs.update(0, () => this.newFirstLeg);
    const newPlan = new Plan(newSource, newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {
    context.removeStartNodeFlag(this.newFirstLeg.source.nodeId);
    context.addStartNodeFlag(this.oldFirstLeg.source.nodeId, this.oldFirstLeg.source.coordinate);
    context.removeRouteLeg(this.newFirstLeg.legId);
    context.addRouteLeg(this.oldFirstLeg.legId);
    const newSource = this.oldFirstLeg.source;
    const newLegs = context.plan().legs.update(0, () => this.oldFirstLeg);
    const newPlan = new Plan(newSource, newLegs);
    context.updatePlan(newPlan);
  }

}
