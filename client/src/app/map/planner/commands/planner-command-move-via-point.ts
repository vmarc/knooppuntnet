import {PlannerContext} from "../context/planner-context";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveViaPoint implements PlannerCommand {

  constructor(private readonly oldLegId1: string,
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

    context.routeLayer.removeFlag(fromLeg1.sinkNode.featureId);
    context.routeLayer.addFlag(PlanFlag.fromViaNode(toLeg1.sinkNode));
    context.routeLayer.removeRouteLeg(fromLeg1.featureId);
    context.routeLayer.removeRouteLeg(fromLeg2.featureId);
    context.routeLayer.addRouteLeg(toLeg1);
    context.routeLayer.addRouteLeg(toLeg2);

    const newLegs = context.plan.legs.map(leg => {
      if (leg.featureId === fromLeg1.featureId) {
        return toLeg1;
      }
      if (leg.featureId === fromLeg2.featureId) {
        return toLeg2;
      }
      return leg;
    });

    const newPlan = PlanUtil.plan(context.plan.sourceNode, newLegs);
    context.updatePlan(newPlan);
  }

}
