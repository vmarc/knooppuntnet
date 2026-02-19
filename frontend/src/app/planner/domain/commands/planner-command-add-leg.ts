import { PlannerContext } from '../context/planner-context';
import { PlannerCommand } from './planner-command';
import { PlanLeg } from '../plan/plan-leg';

export class PlannerCommandAddLeg implements PlannerCommand {
  constructor(private leg: PlanLeg) {}

  do(context: PlannerContext) {
    context.debug('PlannerCommandAddLeg');

    const newLegs = [...context.plan().legs];
    const lastLeg = newLegs.at(-1);
    if (lastLeg) {
      const updatedLastLegSinkFlag =
        lastLeg.viaFlag === null ? lastLeg.sinkFlag.toVia() : lastLeg.sinkFlag.toInvisible();
      const updatedLastLeg = lastLeg.withSinkFlag(updatedLastLegSinkFlag);
      newLegs[newLegs.length - 1] = updatedLastLeg;
      context.markerLayer.updateFlag(updatedLastLeg.sinkFlag);
    }

    newLegs.push(this.leg);

    context.markerLayer.addFlag(this.leg.viaFlag);
    context.markerLayer.addFlag(this.leg.sinkFlag);
    context.routeLayer.addPlanLeg(this.leg);

    const newPlan = context.plan().withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  undo(context: PlannerContext) {
    context.debug('PlannerCommandAddLeg undo');

    context.routeLayer.removePlanLeg(this.leg.featureId);
    context.markerLayer.removeFlag(this.leg.viaFlag);
    context.markerLayer.removeFlag(this.leg.sinkFlag);

    const newLegs = context.plan().legs.slice(0, -1);

    const lastLeg = newLegs.at(-1);
    if (lastLeg) {
      const updatedLastLeg = lastLeg.withSinkFlag(lastLeg.sinkFlag.toEnd());
      newLegs[newLegs.length - 1] = updatedLastLeg;
      context.markerLayer.updateFlag(updatedLastLeg.sinkFlag);
    }

    const newPlan = context.plan().withLegs(newLegs);
    context.updatePlan(newPlan);
  }
}
