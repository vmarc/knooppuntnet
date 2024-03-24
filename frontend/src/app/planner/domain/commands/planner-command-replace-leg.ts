import { PlannerContext } from '../context/planner-context';
import { PlannerCommand } from './planner-command';
import { PlanLeg } from '../plan/plan-leg';

export class PlannerCommandReplaceLeg implements PlannerCommand {
  constructor(
    private readonly oldLeg: PlanLeg,
    private readonly newLeg: PlanLeg
  ) {}

  public do(context: PlannerContext) {
    context.debug('PlannerCommandMoveEndPoint');
    this.update(context, this.oldLeg, this.newLeg);
  }

  public undo(context: PlannerContext) {
    context.debug('PlannerCommandMoveEndPoint undo');
    this.update(context, this.newLeg, this.oldLeg);
  }

  private update(context: PlannerContext, fromLeg: PlanLeg, toLeg: PlanLeg) {
    context.markerLayer.removeFlag(fromLeg.viaFlag);
    context.markerLayer.removeFlag(fromLeg.sinkFlag);
    context.routeLayer.removePlanLeg(fromLeg.featureId);

    context.markerLayer.addFlag(toLeg.viaFlag);
    context.markerLayer.addFlag(toLeg.sinkFlag);
    context.routeLayer.addPlanLeg(toLeg);

    const newLegs = context
      .plan()
      .legs.map((leg) => (leg.featureId === fromLeg.featureId ? toLeg : leg));
    const newPlan = context.plan().withLegs(newLegs);
    context.updatePlan(newPlan);
  }
}
