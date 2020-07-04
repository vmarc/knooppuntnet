import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveFirstLegSource implements PlannerCommand {

  constructor(private readonly oldLegId: string,
              private readonly oldSourceNode: PlanNode,
              private readonly oldSourceFlag: PlanFlag,
              private readonly newLegId: string,
              private readonly newSourceNode: PlanNode,
              private readonly newSourceFlag: PlanFlag) {
  }

  public do(context: PlannerContext) {
    this.update(
      context,
      this.oldLegId,
      this.oldSourceNode,
      this.oldSourceFlag,
      this.newLegId,
      this.newSourceNode,
      this.newSourceFlag
    );
  }

  public undo(context: PlannerContext) {
    this.update(
      context,
      this.newLegId,
      this.newSourceNode,
      this.newSourceFlag,
      this.oldLegId,
      this.oldSourceNode,
      this.oldSourceFlag
    );
  }

  public update(
    context: PlannerContext,
    fromLegId: string,
    fromSourceNode: PlanNode,
    fromSourceFlag: PlanFlag,
    toLegId: string,
    toSourceNode: PlanNode,
    toSourceFlag: PlanFlag
  ) {
    const fromLeg = context.legs.getById(fromLegId);
    const toLeg = context.legs.getById(toLegId);
    context.routeLayer.removeFlag(fromSourceFlag);
    context.routeLayer.updateFlag(toSourceFlag);
    context.routeLayer.removePlanLeg(fromLeg.featureId);
    context.routeLayer.addPlanLeg(toLeg);
    const newLegs = context.plan.legs.update(0, () => toLeg);
    const newPlan = new Plan(toSourceNode, toSourceFlag, newLegs);
    context.updatePlan(newPlan);
  }

}
