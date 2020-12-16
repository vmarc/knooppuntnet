import {PlannerContext} from '../../context/planner-context';
import {PlanUtil} from '../../plan/plan-util';
import {PlannerCommandRemoveViaPoint} from '../../commands/planner-command-remove-via-point';
import {PlannerDragFlag} from '../planner-drag-flag';
import {LegEnd} from '@api/common/planner/leg-end';
import {Observable} from 'rxjs';
import {PlanLeg} from '../../plan/plan-leg';
import {map} from 'rxjs/operators';
import {FeatureId} from '../../features/feature-id';
import {PlanFlag} from '../../plan/plan-flag';

export class RemoveViaPoint {

  constructor(private readonly context: PlannerContext) {
  }

  remove(nodeDrag: PlannerDragFlag): void {

    const legs = this.context.plan.legs;
    const nextLegIndex = legs.findIndex(leg => {
      if (nodeDrag.oldNode !== null) {
        return leg.sourceNode.featureId === nodeDrag.oldNode.featureId;
      }
      return nodeDrag.planFlag.featureId === leg.viaFlag?.featureId;
    });
    const oldLeg1 = legs.get(nextLegIndex - 1);
    const oldLeg2 = legs.get(nextLegIndex);

    const sourceNode = oldLeg1.sourceNode;
    const sinkNode = oldLeg2.sinkNode;
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);

    const sinkFlag = new PlanFlag(oldLeg2.sinkFlag.flagType, FeatureId.next(), sinkNode.coordinate);

    this.buildLeg(source, sink, sinkFlag).subscribe(
      newLeg => {
        const command = new PlannerCommandRemoveViaPoint(oldLeg1, oldLeg2, newLeg);
        this.context.execute(command);
      },
      error => this.context.errorDialog(error)
    );
  }

  private buildLeg(source: LegEnd, sink: LegEnd, sinkFlag: PlanFlag): Observable<PlanLeg> {
    return this.context.fetchLeg(source, sink).pipe(
      map(data => PlanUtil.leg(data, sinkFlag, null))
    );
  }

}
