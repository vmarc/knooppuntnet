import { PlannerContext } from '../context/planner-context';
import { PlannerCommand } from './planner-command';
import { List } from 'immutable';
import { PlanLeg } from '../plan/plan-leg';

export class PlannerCommandRemoveViaPoint implements PlannerCommand {
  constructor(
    private readonly oldLeg1: PlanLeg,
    private readonly oldLeg2: PlanLeg,
    private readonly newLeg: PlanLeg
  ) {}

  public do(context: PlannerContext) {
    context.debug('PlannerCommandRemoveViaPoint');

    context.markerLayer.removeFlag(this.oldLeg1.viaFlag);
    context.markerLayer.removeFlag(this.oldLeg1.sinkFlag);
    context.routeLayer.removePlanLeg(this.oldLeg1.featureId);

    context.markerLayer.removeFlag(this.oldLeg2.viaFlag);
    context.markerLayer.removeFlag(this.oldLeg2.sinkFlag);
    context.routeLayer.removePlanLeg(this.oldLeg2.featureId);

    context.markerLayer.addFlag(this.newLeg.viaFlag);
    context.markerLayer.addFlag(this.newLeg.sinkFlag);
    context.routeLayer.addPlanLeg(this.newLeg);

    const newLegs = context.plan.legs
      .map((leg) => (leg.featureId === this.oldLeg1.featureId ? this.newLeg : leg))
      .filterNot((leg) => leg.featureId === this.oldLeg2.featureId);
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {
    context.debug('PlannerCommandRemoveViaPoint undo');

    context.markerLayer.removeFlag(this.newLeg.sinkFlag);
    context.routeLayer.removePlanLeg(this.newLeg.featureId);

    context.markerLayer.addFlag(this.oldLeg1.viaFlag);
    context.markerLayer.addFlag(this.oldLeg1.sinkFlag);
    context.routeLayer.addPlanLeg(this.oldLeg1);

    context.markerLayer.addFlag(this.oldLeg2.viaFlag);
    context.markerLayer.addFlag(this.oldLeg2.sinkFlag);
    context.routeLayer.addPlanLeg(this.oldLeg2);

    const newLegs = context.plan.legs.flatMap((leg) => {
      if (leg.featureId === this.newLeg.featureId) {
        return List([this.oldLeg1, this.oldLeg2]);
      }
      return List([leg]);
    });
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }
}
