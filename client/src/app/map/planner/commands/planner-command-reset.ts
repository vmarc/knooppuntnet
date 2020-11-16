import {PlannerContext} from '../context/planner-context';
import {Plan} from '../plan/plan';
import {PlannerCommand} from './planner-command';

export class PlannerCommandReset implements PlannerCommand {

  private oldPlan: Plan;

  public do(context: PlannerContext) {
    context.debug('PlannerCommandReset');
    this.oldPlan = context.plan;
    context.routeLayer.removePlan(this.oldPlan);
    context.markerLayer.removePlan(this.oldPlan);
    context.updatePlan(Plan.empty);
  }

  public undo(context: PlannerContext) {
    context.debug('PlannerCommandReset undo');
    context.routeLayer.addPlan(this.oldPlan);
    context.markerLayer.addPlan(this.oldPlan);
    context.updatePlan(this.oldPlan);
  }

}
