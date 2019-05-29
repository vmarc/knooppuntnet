import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveFirstLegSource implements PlannerCommand {

  constructor(private readonly oldFirstLegId: string,
              private readonly newFirstLegId: string) {
  }

  public do(context: PlannerContext) {
    this.update(context, this.oldFirstLegId, this.newFirstLegId);
  }

  public undo(context: PlannerContext) {
    this.update(context, this.newFirstLegId, this.oldFirstLegId);
  }

  public update(context: PlannerContext, fromLegId: string, toLegId: string) {
    const fromLeg = context.legs.getById(fromLegId);
    const toLeg = context.legs.getById(toLegId);
    context.routeLayer.removeFlag(fromLeg.source.featureId);
    context.routeLayer.addFlag(PlanFlag.fromStartNode(toLeg.source));
    context.routeLayer.removeRouteLeg(fromLeg.featureId);
    context.routeLayer.addRouteLeg(toLeg);
    const newSource = toLeg.source;
    const newLegs = context.plan.legs.update(0, () => toLeg);
    const newPlan = Plan.create(newSource, newLegs);
    context.updatePlan(newPlan);
  }

}
