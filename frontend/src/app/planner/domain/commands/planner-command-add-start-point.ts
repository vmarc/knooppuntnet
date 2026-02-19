import { PlanNode } from '@api/common/planner/plan-node';
import { PlannerContext } from '../context/planner-context';
import { Plan } from '../plan/plan';
import { PlanFlag } from '../plan/plan-flag';
import { PlannerCommand } from './planner-command';

export class PlannerCommandAddStartPoint implements PlannerCommand {
  constructor(
    private node: PlanNode,
    private sourceFlag: PlanFlag
  ) {}

  do(context: PlannerContext) {
    context.debug('PlannerCommandAddStartPoint');
    const plan = new Plan(this.node, this.sourceFlag, []);
    context.markerLayer.addFlag(this.sourceFlag);
    context.updatePlan(plan);
  }

  undo(context: PlannerContext) {
    context.debug('PlannerCommandAddStartPoint undo');
    context.updatePlan(Plan.empty);
    context.markerLayer.removeFlag(this.sourceFlag);
  }
}
