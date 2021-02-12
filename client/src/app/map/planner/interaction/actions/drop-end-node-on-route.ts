import {List} from 'immutable';
import {Coordinate} from 'ol/coordinate';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {PlanNode} from '@api/common/planner/plan-node';
import {PlannerCommandReplaceLeg} from '../../commands/planner-command-replace-leg';
import {PlannerContext} from '../../context/planner-context';
import {FeatureId} from '../../features/feature-id';
import {RouteFeature} from '../../features/route-feature';
import {PlanFlag} from '../../plan/plan-flag';
import {PlanFlagType} from '../../plan/plan-flag-type';
import {PlanLeg} from '../../plan/plan-leg';
import {PlanUtil} from '../../plan/plan-util';
import {PlannerDragFlag} from '../planner-drag-flag';

export class DropEndNodeOnRoute {

  constructor(private readonly context: PlannerContext) {
  }

  drop(dragFlag: PlannerDragFlag, routeFeatures: List<RouteFeature>, coordinate: Coordinate): void {
    const oldLeg = this.context.plan.legs.last(null);
    if (oldLeg) {
      this.buildNewLeg(oldLeg.sourceNode, routeFeatures, coordinate).subscribe(
        newLeg => {
          if (newLeg) {
            const command = new PlannerCommandReplaceLeg(oldLeg, newLeg);
            this.context.execute(command);
          }
        },
        error => {
          this.context.resetDragFlag(dragFlag);
          this.context.errorDialog(error);
        }
      );
    }
  }

  private buildNewLeg(sourceNode: PlanNode, routeFeatures: List<RouteFeature>, coordinate: Coordinate): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndRoutes(routeFeatures.toArray());

    return this.context.fetchLeg(source, sink).pipe(
      map(data => {
        const viaFlag = new PlanFlag(PlanFlagType.Via, FeatureId.next(), coordinate);
        const sinkFlag = new PlanFlag(PlanFlagType.End, FeatureId.next(), data.sinkNode.coordinate);
        return PlanUtil.leg(data, sinkFlag, viaFlag);
      })
    );
  }

}
