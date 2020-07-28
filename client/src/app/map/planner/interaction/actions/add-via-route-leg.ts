import {PlannerContext} from "../../context/planner-context";
import {List} from "immutable";
import {RouteFeature} from "../../features/route-feature";
import {Coordinate} from "ol/coordinate";
import {PlanUtil} from "../../plan/plan-util";
import {LegEnd} from "../../../../kpn/api/common/planner/leg-end";
import {TrackPathKey} from "../../../../kpn/api/common/common/track-path-key";
import {LegEndRoute} from "../../../../kpn/api/common/planner/leg-end-route";
import {PlannerCommandAddLeg} from "../../commands/planner-command-add-leg";
import {Observable} from "rxjs";
import {PlanLeg} from "../../plan/plan-leg";
import {map} from "rxjs/operators";

export class AddViaRouteLeg {

  constructor(private readonly context: PlannerContext) {
  }

  add(routes: List<RouteFeature>, coordinate: Coordinate): void {

    // current sink is source for new leg
    const sourceNode = PlanUtil.planSinkNode(this.context.plan);
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);

    const trackPathKeys = routes.flatMap(routeFeature => {
      const trackPathKey = routeFeature.toTrackPathKey();
      if (routeFeature.oneWay) {
        return List([trackPathKey]);
      }
      const extraTrackPathKey = new TrackPathKey(routeFeature.routeId, 100 + routeFeature.pathId);
      return List([trackPathKey, extraTrackPathKey]);
    });

    const sink = new LegEnd(null, new LegEndRoute(trackPathKeys));

    this.buildLeg(source, sink, coordinate).pipe(
      map(leg => new PlannerCommandAddLeg(leg))
    ).subscribe(command => this.context.execute(command));
  }

  private buildLeg(source: LegEnd, sink: LegEnd, coordinate: Coordinate): Observable<PlanLeg> {
    return this.context.fetchLeg(source, sink).pipe(
      map(data => {
        const sinkFlag = PlanUtil.endFlag(data.sinkNode.coordinate);
        const viaFlag = PlanUtil.viaFlag(coordinate);
        return this.context.newLeg(data, sinkFlag, viaFlag);
      })
    );
  }

}
