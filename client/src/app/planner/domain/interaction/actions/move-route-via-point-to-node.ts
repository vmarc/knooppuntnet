import { PlanNode } from '@api/common/planner/plan-node';
import { combineLatest } from 'rxjs';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlannerCommandMoveViaRoute } from '../../commands/planner-command-move-via-route';
import { PlannerContext } from '../../context/planner-context';
import { FeatureId } from '../../features/feature-id';
import { PlanFlag } from '../../plan/plan-flag';
import { PlanFlagType } from '../../plan/plan-flag-type';
import { PlanLeg } from '../../plan/plan-leg';
import { PlanUtil } from '../../plan/plan-util';
import { PlannerDragFlag } from '../planner-drag-flag';
import { PlannerDragViaRouteFlag } from '../planner-drag-via-route-flag';

export class MoveRouteViaPointToNode {
  constructor(private readonly context: PlannerContext) {}

  viaRouteDragMove(
    viaRouteDrag: PlannerDragViaRouteFlag,
    viaNode: PlanNode,
    oldLeg: PlanLeg
  ): void {
    this.move(viaRouteDrag.oldNode, viaRouteDrag.planFlag, viaNode, oldLeg);
  }

  nodeDragMove(
    nodeDrag: PlannerDragFlag,
    viaNode: PlanNode,
    oldLeg: PlanLeg
  ): void {
    this.move(nodeDrag.oldNode, nodeDrag.planFlag, viaNode, oldLeg);
  }

  private move(
    oldNode: PlanNode,
    dragFlag: PlanFlag,
    viaNode: PlanNode,
    oldLeg: PlanLeg
  ): void {
    const newLeg1$ = this.buildNewLeg1(oldLeg.sourceNode, viaNode);
    const newLeg2$ = this.buildNewLeg2(
      viaNode,
      oldLeg.sinkNode,
      oldLeg.sinkFlag
    );

    combineLatest([newLeg1$, newLeg2$])
      .pipe(
        map(
          ([newLeg1, newLeg2]) =>
            new PlannerCommandMoveViaRoute(oldLeg, newLeg1, newLeg2)
        )
      )
      .subscribe({
        next: (command) => this.context.execute(command),
        error: (error) => {
          this.context.markerLayer.updateFlagCoordinate(
            dragFlag.featureId,
            oldNode.coordinate
          );
          this.context.errorDialog(error);
        },
      });
  }

  private buildNewLeg1(
    sourceNode: PlanNode,
    sinkNode: PlanNode
  ): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const sinkFlag = new PlanFlag(
      PlanFlagType.via,
      FeatureId.next(),
      sinkNode.coordinate
    );
    return this.context
      .fetchLeg(source, sink)
      .pipe(map((data) => PlanUtil.leg(data, sinkFlag, null)));
  }

  private buildNewLeg2(
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
