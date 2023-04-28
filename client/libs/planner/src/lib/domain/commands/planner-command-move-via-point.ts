import { PlannerContext } from '../context/planner-context';
import { PlannerCommand } from './planner-command';
import { PlanLeg } from '../plan/plan-leg';

export class PlannerCommandMoveViaPoint implements PlannerCommand {
  constructor(
    private readonly oldLeg1: PlanLeg,
    private readonly oldLeg2: PlanLeg,
    private readonly newLeg1: PlanLeg,
    private readonly newLeg2: PlanLeg
  ) {}

  public do(context: PlannerContext) {
    context.debug('PlannerCommandMoveViaPoint');
    this.update(
      context,
      this.oldLeg1,
      this.oldLeg2,
      this.newLeg1,
      this.newLeg2
    );
  }

  public undo(context: PlannerContext) {
    context.debug('PlannerCommandMoveViaPoint undo');
    this.update(
      context,
      this.newLeg1,
      this.newLeg2,
      this.oldLeg1,
      this.oldLeg2
    );
  }

  private update(
    context: PlannerContext,
    fromLeg1: PlanLeg,
    fromLeg2: PlanLeg,
    toLeg1: PlanLeg,
    toLeg2: PlanLeg
  ) {
    context.markerLayer.removeFlag(fromLeg1.sinkFlag);
    context.markerLayer.addFlag(toLeg1.sinkFlag);
    context.routeLayer.removePlanLeg(fromLeg1.featureId);
    context.routeLayer.removePlanLeg(fromLeg2.featureId);
    context.routeLayer.addPlanLeg(toLeg1);
    context.routeLayer.addPlanLeg(toLeg2);

    const newLegs = context.plan.legs.map((leg) => {
      if (leg.featureId === fromLeg1.featureId) {
        return toLeg1;
      }
      if (leg.featureId === fromLeg2.featureId) {
        return toLeg2;
      }
      return leg;
    });

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }
}
