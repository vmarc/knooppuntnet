import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {Util} from "../../../components/shared/util";
import {Bounds} from "../../../kpn/api/common/bounds";
import {LatLonImpl} from "../../../kpn/api/common/lat-lon-impl";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {LegEndNode} from "../../../kpn/api/common/planner/leg-end-node";
import {LegEndRoute} from "../../../kpn/api/common/planner/leg-end-route";
import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlanLeg} from "../../../kpn/api/common/planner/plan-leg";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlanRoute} from "../../../kpn/api/common/planner/plan-route";
import {FeatureId} from "../features/feature-id";

export class PlanUtil {

  static readonly emptyPlan = new Plan(null, List());

  static toUrlString(plan: Plan): string {

    let legEnds: List<LegEnd> = List();

    if (plan.sourceNode !== null) {
      legEnds = legEnds.push(PlanUtil.legEndNode(+plan.sourceNode.nodeId));
    }

    plan.legs.forEach(leg => {
      legEnds = legEnds.push(leg.sink);
    });

    return legEnds.map(legEnd => PlanUtil.encodedLegEndKey(legEnd)).join("-");
  }

  static toNodeIds(planUrlString: string): List<string> {
    const nodeIdsRadix36: List<string> = List(planUrlString.split("-"));
    return nodeIdsRadix36.map(nodeId => parseInt(nodeId, 36).toString());
  }

  static distinctColours(colours: List<string>): List<string> {
    return colours.reduce((unique, colour) => {
      if (unique.isEmpty() || (unique.last() !== colour)) {
        return unique.push(colour);
      }
      return unique;
    }, List<string>());
  }

  static key(source: LegEnd, sink: LegEnd): string {
    return PlanUtil.legEndKey(source) + ">" + PlanUtil.legEndKey(sink);
  }

  static legEndNode(nodeId: number): LegEnd {
    return new LegEnd(new LegEndNode(nodeId), null);
  }

  static legEndRoute(routeId: number, pathId: number): LegEnd {
    return new LegEnd(null, new LegEndRoute(routeId, pathId));
  }

  static legEndKey(legEnd: LegEnd): string {
    if (legEnd.node !== null) {
      return legEnd.node.nodeId.toString();
    }
    if (legEnd.route !== null) {
      return legEnd.route.routeId.toString() + "." + legEnd.route.pathId.toString();
    }
    return "";
  }

  static encodedLegEndKey(legEnd: LegEnd): string {
    if (legEnd.node !== null) {
      return legEnd.node.nodeId.toString(36);
    }
    if (legEnd.route !== null) {
      return legEnd.route.routeId.toString(36) + "." + legEnd.route.pathId.toString(36);
    }
    return "";
  }

  static planNode(nodeId: string, nodeName: string, latLon: LatLonImpl): PlanNode {
    const coordinate = Util.latLonToCoordinate(latLon);
    return new PlanNode(FeatureId.next(), nodeId, nodeName, coordinate, latLon);
  }

  static planNodeWithCoordinate(nodeId: string, nodeName: string, coordinate: Coordinate): PlanNode {
    const latLon = Util.latLonFromCoordinate(coordinate);
    return new PlanNode(FeatureId.next(), nodeId, nodeName, coordinate, latLon);
  }

  static planSinkNode(plan: Plan): PlanNode {
    const lastLeg = plan.legs.last(null);
    if (lastLeg) {
      return lastLeg.sinkNode;
    }
    return plan.sourceNode;
  }

  static plan(source: PlanNode, legs: List<PlanLeg>): Plan {
    return new Plan(source, legs);
  }

  static planMeters(plan: Plan): number {
    return plan.legs.map(l => l.meters).reduce((sum, current) => sum + current, 0);
  }

  static planCumulativeMetersLeg(plan: Plan, legIndex: number): number {
    if (legIndex < plan.legs.size) {
      return plan.legs.slice(0, legIndex + 1).map(l => l.meters).reduce((sum, current) => sum + current, 0);
    }
    return 0;
  }

  static planCumulativeKmLeg(plan: Plan, legIndex: number): string {
    const meters = PlanUtil.planCumulativeMetersLeg(plan, legIndex);
    const km = Math.round(meters / 100) / 10;
    const kmString = parseFloat(km.toFixed(1));
    return kmString + " km";
  }

  static planBounds(plan: Plan): Bounds {
    if (plan.sourceNode === null) {
      return null;
    }
    const latLons: List<LatLonImpl> = List([plan.sourceNode.latLon])
      .concat(plan.legs.flatMap(leg => List([leg.sourceNode.latLon])
        .concat(leg.routes.flatMap(route => PlanUtil.planRouteLatLons(route)))));

    const lats = latLons.map(latLon => +latLon.latitude);
    const lons = latLons.map(latLon => +latLon.longitude);
    const minLat = lats.min();
    const minLon = lons.min();
    const maxLat = lats.max();
    const maxLon = lons.max();
    return new Bounds(minLat, minLon, maxLat, maxLon);
  }

  static planUnpavedPercentage(plan: Plan): string {
    const distances: List<number> = plan.legs.flatMap(leg => {
      return leg.routes.flatMap(route => {
        return route.segments.filter(segment => segment.surface === "unpaved").map(segment => segment.meters);
      });
    });
    const unpavedMeters = distances.reduce((sum, current) => sum + current, 0);
    const percentage = Math.round(100 * unpavedMeters / PlanUtil.planMeters(plan));
    return `${percentage}%`;
  }

  static planRouteLatLons(planRoute: PlanRoute): List<LatLonImpl> {
    return List([planRoute.sourceNode.latLon])
      .concat(planRoute.segments.flatMap(segment => segment.fragments.map(fragment => fragment.latLon)));
  }

  static planRouteCoordinates(planRoute: PlanRoute): List<Coordinate> {
    return List([planRoute.sourceNode.coordinate])
      .concat(planRoute.segments.flatMap(segment => segment.fragments.map(fragment => fragment.coordinate)));
  }

}
