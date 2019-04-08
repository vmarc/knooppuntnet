import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveStartPoint implements PlannerCommand {

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
    context.routeLayer.removeStartNodeFlag(fromLeg.source.nodeId);
    context.routeLayer.addStartNodeFlag(toLeg.source.nodeId, toLeg.source.coordinate);
    context.routeLayer.removeRouteLeg(fromLeg.legId);
    context.routeLayer.addRouteLeg(toLeg);
    const newSource = toLeg.source;
    const newLegs = context.plan.legs.update(0, () => toLeg);
    const newPlan = new Plan(newSource, newLegs);
    context.updatePlan(newPlan);
  }

}
