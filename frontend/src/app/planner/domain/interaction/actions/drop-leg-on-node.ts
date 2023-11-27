import { PlanNode } from '@api/common/planner';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { switchMap } from 'rxjs/operators';
import { PlannerCommandSplitLeg } from '../../commands/planner-command-split-leg';
import { PlannerContext } from '../../context/planner-context';
import { FeatureId } from '../../features/feature-id';
import { PlanFlag } from '../../plan/plan-flag';
import { PlanFlagType } from '../../plan/plan-flag-type';
import { PlanLeg } from '../../plan/plan-leg';
import { PlanUtil } from '../../plan/plan-util';
import { PlannerDragLeg } from '../planner-drag-leg';

export class DropLegOnNode {
  constructor(private readonly context: PlannerContext) {}

  drop(legDrag: PlannerDragLeg, connection: PlanNode): void {
    const oldLeg = this.context.plan.legs.find((leg) => leg.featureId === legDrag.oldLegId);
    if (oldLeg) {
      this.buildLeg1(oldLeg.sourceNode, connection)
        .pipe(
          switchMap((newLeg1) =>
            this.buildLeg2(connection, oldLeg.sinkNode).pipe(
              map((newLeg2) => new PlannerCommandSplitLeg(oldLeg, newLeg1, newLeg2))
            )
          )
        )
        .subscribe({
          next: (command) => this.context.execute(command),
          error: (error) => this.context.errorDialog(error),
        });
    }
  }

  private buildLeg1(sourceNode: PlanNode, sinkNode: PlanNode): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const sinkFlag = PlanUtil.viaFlag(sinkNode.coordinate);

    return this.context
      .fetchLeg(source, sink)
      .pipe(map((data) => PlanUtil.leg(data, sinkFlag, null)));
  }

  private buildLeg2(sourceNode: PlanNode, sinkNode: PlanNode): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const isLastLeg = sinkNode.featureId === this.context.plan.sinkNode().featureId;
    const sinkFlagType = isLastLeg ? PlanFlagType.end : PlanFlagType.via;
    const sinkFlag = new PlanFlag(sinkFlagType, FeatureId.next(), sinkNode.coordinate);

    return this.context
      .fetchLeg(source, sink)
      .pipe(map((data) => PlanUtil.leg(data, sinkFlag, null)));
  }
}
