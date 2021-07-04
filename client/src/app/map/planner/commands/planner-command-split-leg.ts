import { PlannerContext } from '../context/planner-context';
import { PlannerCommand } from './planner-command';
import { PlanLeg } from '../plan/plan-leg';
import { List } from 'immutable';

export class PlannerCommandSplitLeg implements PlannerCommand {
  constructor(
    private oldLeg: PlanLeg,
    private newLeg1: PlanLeg,
    private newLeg2: PlanLeg
  ) {}

  public do(context: PlannerContext) {
    context.debug('PlannerCommandSplitLeg');

    context.markerLayer.removeFlag(this.oldLeg.viaFlag);
    context.markerLayer.removeFlag(this.oldLeg.sinkFlag);
    context.routeLayer.removePlanLeg(this.oldLeg.featureId);

    context.markerLayer.addFlag(this.newLeg1.viaFlag);
    context.markerLayer.addFlag(this.newLeg1.sinkFlag);
    context.routeLayer.addPlanLeg(this.newLeg1);

    context.markerLayer.addFlag(this.newLeg2.viaFlag);
    context.markerLayer.addFlag(this.newLeg2.sinkFlag);
    context.routeLayer.addPlanLeg(this.newLeg2);

    const newLegs = context.plan.legs.flatMap((leg) => {
      if (leg.featureId === this.oldLeg.featureId) {
        return List([this.newLeg1, this.newLeg2]);
      }
      return List([leg]);
    });

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {
    context.debug('PlannerCommandSplitLeg undo');

    context.markerLayer.removeFlag(this.newLeg1.viaFlag);
    context.markerLayer.removeFlag(this.newLeg1.sinkFlag);
    context.routeLayer.removePlanLeg(this.newLeg1.featureId);

    context.markerLayer.removeFlag(this.newLeg2.viaFlag);
    context.markerLayer.removeFlag(this.newLeg2.sinkFlag);
    context.routeLayer.removePlanLeg(this.newLeg2.featureId);

    context.markerLayer.addFlag(this.oldLeg.viaFlag);
    context.markerLayer.addFlag(this.oldLeg.sinkFlag);
    context.routeLayer.addPlanLeg(this.oldLeg);

    const newLegs = context.plan.legs.flatMap((leg) => {
      if (leg.featureId === this.newLeg1.featureId) {
        return List([this.oldLeg]);
      }
      if (leg.featureId === this.newLeg2.featureId) {
        return List();
      }
      return List([leg]);
    });

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }
}
