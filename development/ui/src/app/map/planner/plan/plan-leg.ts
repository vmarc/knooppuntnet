import {List} from "immutable";
import Coordinate from "ol/coordinate";
import {fromLonLat} from "ol/proj";
import {RouteLeg} from "../../../kpn/shared/planner/route-leg";
import {RouteLegNode} from "../../../kpn/shared/planner/route-leg-node";
import {PlanFragment} from "./plan-fragment";
import {PlanNode} from "./plan-node";
import {PlanRoute} from "./plan-route";
import {PlanSegment} from "./plan-segment";

export class PlanLeg {

  constructor(public readonly featureId: string,
              public readonly source: PlanNode,
              public readonly sink: PlanNode,
              public readonly meters: number,
              public readonly routes: List<PlanRoute>) {
  }

  coordinates(): List<Coordinate> {
    return this.routes.flatMap(route => route.coordinates());
  }

  static fromRouteLeg(routeLeg: RouteLeg): PlanLeg {

    const routes = routeLeg.routes.map(routeLegRoute => {
      const source = this.toPlanNode(routeLegRoute.source);
      const sink = this.toPlanNode(routeLegRoute.sink);

      const segments: List<PlanSegment> = routeLegRoute.segments.map(routeLegSegment => {
        const fragments = routeLegSegment.fragments.map(fragment => {
          const lon = parseFloat(fragment.lon);
          const lat = parseFloat(fragment.lat);
          const coordinate = fromLonLat([lon, lat]);

          return new PlanFragment(
            fragment.meters,
            fragment.orientation,
            fragment.streetIndex,
            coordinate
          );
        });

        return new PlanSegment(routeLegSegment.meters, routeLegSegment.surface, fragments);
      });

      return new PlanRoute(source, sink, routeLegRoute.meters, segments, routeLegRoute.streets);
    });

    const source = routes.get(0).source;
    const lastRoute: PlanRoute = routes.last();
    const sink = lastRoute.sink;
    const meters = routes.map(f => f.meters).reduce((sum, current) => sum + current, 0);

    return new PlanLeg(routeLeg.legId, source, sink, meters, routes);
  }

  private static toPlanNode(routeLegNode: RouteLegNode): PlanNode {
    const nodeId: string = routeLegNode.nodeId;
    const nodeName: string = routeLegNode.nodeName;
    const lon = parseFloat(routeLegNode.lat);
    const lat = parseFloat(routeLegNode.lon);
    const coordinate: Coordinate = fromLonLat([lon, lat]);
    return PlanNode.create(nodeId, nodeName, coordinate);
  }

}
