import { PlanNode } from '@api/common/planner';
import { List } from 'immutable';
import { PlannerContext } from '../context/planner-context';
import { Plan } from '../plan/plan';
import { PlannerCommand } from './planner-command';

export class PlannerCommandMoveStartPoint implements PlannerCommand {
  constructor(
    private readonly oldNode: PlanNode,
    private readonly newNode: PlanNode
  ) {}

  public do(context: PlannerContext) {
    context.debug('PlannerCommandMoveStartPoint');
    this.update(context, this.oldNode, this.newNode);
  }

  public undo(context: PlannerContext) {
    context.debug('PlannerCommandMoveStartPoint undo');
    this.update(context, this.newNode, this.oldNode);
  }

  public update(context: PlannerContext, fromNode: PlanNode, toNode: PlanNode) {
    context.markerLayer.removeFlag(context.plan.sourceFlag);
    const newFlag = context.plan.sourceFlag.withCoordinate(toNode.coordinate);
    context.markerLayer.addFlag(newFlag);
    const newPlan = new Plan(toNode, newFlag, List());
    context.updatePlan(newPlan);
  }
}
