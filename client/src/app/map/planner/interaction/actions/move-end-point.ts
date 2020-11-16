import {PlannerContext} from '../../context/planner-context';
import {PlanNode} from '../../../../kpn/api/common/planner/plan-node';
import {PlanUtil} from '../../plan/plan-util';
import {PlannerCommandReplaceLeg} from '../../commands/planner-command-replace-leg';
import {LegEnd} from '../../../../kpn/api/common/planner/leg-end';
import {Observable} from 'rxjs';
import {PlanLeg} from '../../plan/plan-leg';
import {map} from 'rxjs/operators';
import {PlannerDragFlag} from '../planner-drag-flag';

export class MoveEndPoint {

  constructor(private readonly context: PlannerContext) {
  }

  move(dragFlag: PlannerDragFlag, sinkNode: PlanNode): void {
    const oldLeg = this.context.plan.legs.last(null);
    if (oldLeg) {
      const sourceNode = oldLeg.sourceNode;
      const source = PlanUtil.legEndNode(+sourceNode.nodeId);
      const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
      this.buildLeg(source, sink).pipe(
        map(newLeg => new PlannerCommandReplaceLeg(oldLeg, newLeg))
      ).subscribe(
        command => this.context.execute(command),
        error => {
          this.context.resetDragFlag(dragFlag);
          this.context.errorDialog(error);
        }
      );
    }
  }

  private buildLeg(source: LegEnd, sink: LegEnd): Observable<PlanLeg> {
    return this.context.fetchLeg(source, sink).pipe(
      map(data => {
        const sinkFlag = PlanUtil.endFlag(data.sinkNode.coordinate);
        return PlanUtil.leg(data, sinkFlag, null);
      })
    );
  }

}
