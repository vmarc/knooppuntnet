import { LegEnd } from '@api/common/planner/leg-end';
import { PlanNode } from '@api/common/planner/plan-node';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlannerCommandReplaceLeg } from '../../commands/planner-command-replace-leg';
import { PlannerContext } from '../../context/planner-context';
import { PlanLeg } from '../../plan/plan-leg';
import { PlanUtil } from '../../plan/plan-util';
import { PlannerDragFlag } from '../planner-drag-flag';

export class MoveEndPoint {
  constructor(private readonly context: PlannerContext) {}

  move(dragFlag: PlannerDragFlag, sinkNode: PlanNode): void {
    const oldLeg = this.context.plan.legs.last(null);
    if (oldLeg) {
      const sourceNode = oldLeg.sourceNode;
      const source = PlanUtil.legEndNode(+sourceNode.nodeId);
      const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
      this.buildLeg(source, sink)
        .pipe(map((newLeg) => new PlannerCommandReplaceLeg(oldLeg, newLeg)))
        .subscribe({
          next: (command) => this.context.execute(command),
          error: (error) => {
            this.context.resetDragFlag(dragFlag);
            this.context.errorDialog(error);
          },
        });
    }
  }

  private buildLeg(source: LegEnd, sink: LegEnd): Observable<PlanLeg> {
    return this.context.fetchLeg(source, sink).pipe(
      map((data) => {
        const sinkFlag = PlanUtil.endFlag(data.sinkNode.coordinate);
        return PlanUtil.leg(data, sinkFlag, null);
      })
    );
  }
}
