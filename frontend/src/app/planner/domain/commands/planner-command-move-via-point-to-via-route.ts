import { List } from 'immutable';
import { PlannerContext } from '../context/planner-context';
import { PlanLeg } from '../plan/plan-leg';
import { PlannerCommand } from './planner-command';

export class PlannerCommandMoveViaPointToViaRoute implements PlannerCommand {
  constructor(
    private readonly oldLeg1: PlanLeg,
    private readonly oldLeg2: PlanLeg,
    private readonly newLeg1: PlanLeg,
    private readonly newLeg2: PlanLeg
  ) {}

  public do(context: PlannerContext) {
    context.debug('PlannerCommandMoveViaPointToViaRoute');
    this.update(context, this.oldLeg1, this.oldLeg2, this.newLeg1, this.newLeg2);
  }

  public undo(context: PlannerContext) {
    context.debug('PlannerCommandMoveViaPointToViaRoute undo');
    this.update(context, this.newLeg1, this.newLeg2, this.oldLeg1, this.oldLeg2);
  }

  private update(
    context: PlannerContext,
    fromLeg1: PlanLeg,
    fromLeg2: PlanLeg,
    toLeg1: PlanLeg,
    toLeg2: PlanLeg
  ) {
    context.markerLayer.removeFlag(fromLeg1.viaFlag);
    context.markerLayer.removeFlag(fromLeg1.sinkFlag);
    context.markerLayer.removeFlag(fromLeg2.viaFlag);
    context.markerLayer.removeFlag(fromLeg2.sinkFlag);
    context.routeLayer.removePlanLeg(fromLeg1.featureId);
    context.routeLayer.removePlanLeg(fromLeg2.featureId);

    context.markerLayer.addFlag(toLeg1.viaFlag);
    context.markerLayer.addFlag(toLeg1.sinkFlag);
    context.markerLayer.addFlag(toLeg2.viaFlag);
    context.markerLayer.addFlag(toLeg2.sinkFlag);
    context.routeLayer.addPlanLeg(toLeg1);
    context.routeLayer.addPlanLeg(toLeg2);

    const newLegs: List<PlanLeg> = context.plan().legs.map((leg) => {
      if (leg.featureId === fromLeg1.featureId) {
        return toLeg1;
      }
      if (leg.featureId === fromLeg2.featureId) {
        return toLeg2;
      }
      return leg;
    });
    const newPlan = context.plan().withLegs(newLegs);
    context.updatePlan(newPlan);
  }
}
