import { LatLonImpl } from '@api/common/lat-lon-impl';
import { PlanFragment } from '@api/common/planner/plan-fragment';
import { PlanNode } from '@api/common/planner/plan-node';
import { PlanRoute } from '@api/common/planner/plan-route';
import { PlanSegment } from '@api/common/planner/plan-segment';
import { RouteLeg } from '@api/common/planner/route-leg';
import { RouteLegFragment } from '@api/common/planner/route-leg-fragment';
import { RouteLegNode } from '@api/common/planner/route-leg-node';
import { RouteLegRoute } from '@api/common/planner/route-leg-route';
import { RouteLegSegment } from '@api/common/planner/route-leg-segment';
import { List } from 'immutable';
import { Util } from '../../../components/shared/util';
import { FeatureId } from '../features/feature-id';
import { PlanFlag } from './plan-flag';
import { PlanLeg } from './plan-leg';
import { PlanUtil } from './plan-util';

export class PlanLegBuilder {
  static toPlanLeg2(routeLeg: RouteLeg): PlanLeg {
    const routes = routeLeg.routes.map((routeLegRoute) =>
      this.toPlanRoute(routeLegRoute)
    );
    const sourceNode = routes.get(0).sourceNode;
    const lastRoute: PlanRoute = routes.last();
    const sinkNode = lastRoute.sinkNode;

    // TODO PLAN further work out this temporary code
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);

    const legKey = PlanUtil.key(source, sink);
    return new PlanLeg(
      routeLeg.legId,
      legKey,
      source,
      sink,
      PlanFlag.end(FeatureId.next(), sinkNode.coordinate),
      null,
      routes
    );
  }

  private static toPlanRoute(route: RouteLegRoute): PlanRoute {
    const source = this.toPlanNode(route.source);
    const sink = this.toPlanNode(route.sink);
    const segments: List<PlanSegment> = route.segments.map((s) =>
      this.toPlanSegment(s)
    );
    return {
      sourceNode: source,
      sinkNode: sink,
      meters: route.meters,
      segments: segments.toArray(),
      streets: route.streets.toArray(),
    };
  }

  private static toPlanSegment(segment: RouteLegSegment): PlanSegment {
    const fragments = segment.fragments.map((f) => this.toPlanFragment(f));
    return {
      meters: segment.meters,
      surface: segment.surface,
      colour: segment.colour,
      fragments: fragments.toArray(),
    };
  }

  private static toPlanFragment(fragment: RouteLegFragment): PlanFragment {
    const latLon: LatLonImpl = {
      latitude: fragment.lat,
      longitude: fragment.lon,
    };
    const coordinate = Util.latLonToCoordinate(latLon);
    return {
      meters: fragment.meters,
      orientation: fragment.orientation,
      streetIndex: fragment.streetIndex,
      coordinate,
      latLon,
    };
  }

  private static toPlanNode(routeLegNode: RouteLegNode): PlanNode {
    const nodeId: string = routeLegNode.nodeId;
    const nodeName: string = routeLegNode.nodeName;
    const latLon: LatLonImpl = {
      latitude: routeLegNode.lat,
      longitude: routeLegNode.lon,
    };
    return PlanUtil.planNode(nodeId, nodeName, null, latLon);
  }
}
