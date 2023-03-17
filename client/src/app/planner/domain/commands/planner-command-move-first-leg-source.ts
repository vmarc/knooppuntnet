import { PlanNode } from '@api/common/planner/plan-node';
import { PlannerContext } from '../context/planner-context';
import { Plan } from '../plan/plan';
import { PlanFlag } from '../plan/plan-flag';
import { PlannerCommand } from './planner-command';
import { PlanLeg } from '../plan/plan-leg';

export class PlannerCommandMoveFirstLegSource implements PlannerCommand {
  constructor(
    private readonly oldLeg: PlanLeg,
    private readonly oldSourceNode: PlanNode,
    private readonly oldSourceFlag: PlanFlag,
    private readonly newLeg: PlanLeg,
    private readonly newSourceNode: PlanNode,
    private readonly newSourceFlag: PlanFlag
  ) {}

  public do(context: PlannerContext) {
    context.debug('PlannerCommandMoveFirstLegSource');
    this.update(
      context,
      this.oldLeg,
      this.oldSourceNode,
      this.oldSourceFlag,
      this.newLeg,
      this.newSourceNode,
      this.newSourceFlag
    );
  }

  public undo(context: PlannerContext) {
    context.debug('PlannerCommandMoveFirstLegSource undo');
    this.update(
      context,
      this.newLeg,
      this.newSourceNode,
      this.newSourceFlag,
      this.oldLeg,
      this.oldSourceNode,
      this.oldSourceFlag
    );
  }

  public update(
    context: PlannerContext,
    fromLeg: PlanLeg,
    fromSourceNode: PlanNode,
    fromSourceFlag: PlanFlag,
    toLeg: PlanLeg,
    toSourceNode: PlanNode,
    toSourceFlag: PlanFlag
  ) {
    context.markerLayer.removeFlag(fromSourceFlag);
    context.markerLayer.removeFlag(fromLeg.viaFlag);
    context.markerLayer.removeFlag(fromLeg.sinkFlag);
    context.routeLayer.removePlanLeg(fromLeg.featureId);

    context.markerLayer.updateFlag(toSourceFlag);
    context.markerLayer.updateFlag(toLeg.viaFlag);
    context.markerLayer.updateFlag(toLeg.sinkFlag);
    context.routeLayer.addPlanLeg(toLeg);
    const newLegs = context.plan.legs.map((leg) =>
      leg.featureId === fromLeg.featureId ? toLeg : leg
    );
    const newPlan = new Plan(toSourceNode, toSourceFlag, newLegs);
    context.updatePlan(newPlan);
  }
}
