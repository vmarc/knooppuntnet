import { PlanNode } from '@api/common/planner';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlannerCommandRemoveViaPoint } from '../../commands/planner-command-remove-via-point';
import { PlannerContext } from '../../context/planner-context';
import { PlanFlag } from '../../plan/plan-flag';
import { PlanLeg } from '../../plan/plan-leg';
import { PlanUtil } from '../../plan/plan-util';

export class RemoveViaLegRouteViaPoint {
  constructor(private readonly context: PlannerContext) {}

  remove(oldLeg1: PlanLeg): void {
    const oldLeg2 = this.context
      .plan()
      .legs.find((leg) => leg.sourceNode.nodeId === oldLeg1.sinkNode.nodeId);

    if (oldLeg2 != null) {
      this.buildNewLeg(oldLeg1.sourceNode, oldLeg2.sinkNode, oldLeg2.sinkFlag).subscribe({
        next: (newLeg) => {
          const command = new PlannerCommandRemoveViaPoint(oldLeg1, oldLeg2, newLeg);
          this.context.execute(command);
        },
        error: (error) => this.context.errorDialog(error),
      });
    }
  }

  private buildNewLeg(
    sourceNode: PlanNode,
    sinkNode: PlanNode,
    sinkFlag: PlanFlag
  ): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    return this.context
      .fetchLeg(source, sink)
      .pipe(map((data) => PlanUtil.leg(data, sinkFlag, null)));
  }
}
