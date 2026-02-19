import { TrackPathKey } from '@api/common/common/track-path-key';
import { LegEnd } from '@api/common/planner/leg-end';
import { LegEndRoute } from '@api/common/planner/leg-end-route';
import { Coordinate } from 'ol/coordinate';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlannerCommandAddLeg } from '../../commands/planner-command-add-leg';
import { PlannerContext } from '../../context/planner-context';
import { RouteFeature } from '../../features/route-feature';
import { PlanLeg } from '../../plan/plan-leg';
import { PlanUtil } from '../../plan/plan-util';

export class AddViaRouteLeg {
  constructor(private readonly context: PlannerContext) {}

  add(routes: RouteFeature[], coordinate: Coordinate): void {
    // the current sink is the source for the new leg
    const sourceNode = PlanUtil.planSinkNode(this.context.plan());
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);

    const trackPathKeys = routes.flatMap((routeFeature) => {
      const trackPathKey = routeFeature.toTrackPathKey();
      if (routeFeature.oneWay) {
        return [trackPathKey];
      }
      const extraTrackPathKey: TrackPathKey = {
        routeId: routeFeature.routeId,
        pathId: 100 + routeFeature.pathId,
      };
      return [trackPathKey, extraTrackPathKey];
    });

    const legEndRoute: LegEndRoute = {
      trackPathKeys: trackPathKeys,
      selection: null,
    };

    const sink: LegEnd = {
      node: null,
      route: legEndRoute,
    };

    this.buildLeg(source, sink, coordinate)
      .pipe(map((leg) => new PlannerCommandAddLeg(leg)))
      .subscribe({
        next: (command) => this.context.execute(command),
        error: (error) => this.context.errorDialog(error),
      });
  }

  private buildLeg(source: LegEnd, sink: LegEnd, coordinate: Coordinate): Observable<PlanLeg> {
    return this.context.fetchLeg(source, sink).pipe(
      map((data) => {
        const sinkFlag = PlanUtil.endFlag(data.sinkNode.coordinate);
        const viaFlag = PlanUtil.viaFlag(coordinate);
        return PlanUtil.leg(data, sinkFlag, viaFlag);
      })
    );
  }
}
