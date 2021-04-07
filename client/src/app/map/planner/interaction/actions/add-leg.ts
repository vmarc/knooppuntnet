import { PlanNode } from '@api/common/planner/plan-node';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlannerCommandAddLeg } from '../../commands/planner-command-add-leg';
import { PlannerContext } from '../../context/planner-context';
import { PlanLeg } from '../../plan/plan-leg';
import { PlanUtil } from '../../plan/plan-util';

export class AddLeg {
  constructor(private readonly context: PlannerContext) {}

  add(sinkNode: PlanNode): void {
    this.buildLeg(sinkNode)
      .pipe(map((leg) => new PlannerCommandAddLeg(leg)))
      .subscribe(
        (command) => this.context.execute(command),
        (error) => this.context.errorDialog(error)
      );
  }

  private buildLeg(sinkNode: PlanNode): Observable<PlanLeg> {
    const sourceNode = this.context.plan.sinkNode();
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const sinkFlag = PlanUtil.endFlag(sinkNode.coordinate);
    return this.context
      .fetchLeg(source, sink)
      .pipe(map((data) => PlanUtil.leg(data, sinkFlag, null)));
  }
}
