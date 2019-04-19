import {List} from "immutable";
import Coordinate from "ol/coordinate";
import {fromLonLat} from "ol/proj";
import {RouteLeg} from "../../../kpn/shared/planner/route-leg";
import {RouteLegFragment} from "../../../kpn/shared/planner/route-leg-fragment";
import {RouteLegNode} from "../../../kpn/shared/planner/route-leg-node";
import {RouteLegRoute} from "../../../kpn/shared/planner/route-leg-route";
import {RouteLegSegment} from "../../../kpn/shared/planner/route-leg-segment";
import {PlanFragment} from "./plan-fragment";
import {PlanLeg} from "./plan-leg";
import {PlanNode} from "./plan-node";
import {PlanRoute} from "./plan-route";
import {PlanSegment} from "./plan-segment";

export class PlanLegBuilder {

  static toPlanLeg(source: PlanNode, sink: PlanNode, routeLeg: RouteLeg): PlanLeg {
    const routes = routeLeg.routes.map(routeLegRoute => this.toPlanRoute(routeLegRoute));
    const meters = routes.map(f => f.meters).reduce((sum, current) => sum + current, 0);
    return new PlanLeg(routeLeg.legId, source, sink, meters, routes);
  }

  static toPlanLeg2(routeLeg: RouteLeg): PlanLeg {
    const routes = routeLeg.routes.map(routeLegRoute => this.toPlanRoute(routeLegRoute));
    const source = routes.get(0).source;
    const lastRoute: PlanRoute = routes.last();
    const sink = lastRoute.sink;
    const meters = routes.map(f => f.meters).reduce((sum, current) => sum + current, 0);
    return new PlanLeg(routeLeg.legId, source, sink, meters, routes);
  }

  private static toPlanRoute(route: RouteLegRoute): PlanRoute {
    const source = this.toPlanNode(route.source);
    const sink = this.toPlanNode(route.sink);
    const segments: List<PlanSegment> = route.segments.map(s => this.toPlanSegment(s));
    return new PlanRoute(source, sink, route.meters, segments, route.streets);
  }

  private static toPlanSegment(segment: RouteLegSegment): PlanSegment {
    const fragments = segment.fragments.map(f => this.toPlanFragment(f));
    return new PlanSegment(segment.meters, segment.surface, fragments);
  }

  private static toPlanFragment(fragment: RouteLegFragment): PlanFragment {
    const coordinate = this.latLonToCoordinate(fragment.lat, fragment.lon);
    return new PlanFragment(
      fragment.meters,
      fragment.orientation,
      fragment.streetIndex,
      coordinate
    );
  }

  private static toPlanNode(routeLegNode: RouteLegNode): PlanNode {
    const nodeId: string = routeLegNode.nodeId;
    const nodeName: string = routeLegNode.nodeName;
    const coordinate = this.latLonToCoordinate(routeLegNode.lat, routeLegNode.lon);
    return PlanNode.create(nodeId, nodeName, coordinate);
  }

  private static latLonToCoordinate(lat: string, lon: string): Coordinate {
    const latNumber = parseFloat(lat);
    const lonNumber = parseFloat(lon);
    return fromLonLat([lonNumber, latNumber]);
  }

}
