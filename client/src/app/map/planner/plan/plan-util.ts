import {List} from 'immutable';
import {Coordinate} from 'ol/coordinate';
import {Util} from '../../../components/shared/util';
import {LatLonImpl} from '../../../kpn/api/common/lat-lon-impl';
import {LegEnd} from '../../../kpn/api/common/planner/leg-end';
import {LegEndNode} from '../../../kpn/api/common/planner/leg-end-node';
import {LegEndRoute} from '../../../kpn/api/common/planner/leg-end-route';
import {PlanFragment} from '../../../kpn/api/common/planner/plan-fragment';
import {PlanNode} from '../../../kpn/api/common/planner/plan-node';
import {PlanRoute} from '../../../kpn/api/common/planner/plan-route';
import {PlanSegment} from '../../../kpn/api/common/planner/plan-segment';
import {FeatureId} from '../features/feature-id';
import {RouteFeature} from '../features/route-feature';
import {Plan} from './plan';
import {PlanFlag} from './plan-flag';
import {PlanLeg} from './plan-leg';
import {PlanFlagType} from './plan-flag-type';
import {PlanLegData} from '../context/plan-leg-data';
import {TrackPathKey} from '../../../kpn/api/common/common/track-path-key';

export class PlanUtil {

  static toUrlString(plan: Plan): string {

    let legEnds: List<LegEnd> = List();

    if (plan.sourceNode !== null) {
      legEnds = legEnds.push(PlanUtil.legEndNode(+plan.sourceNode.nodeId));
    }

    plan.legs.forEach(leg => {
      legEnds = legEnds.push(leg.sink);
    });

    return legEnds.map(legEnd => PlanUtil.encodedLegEndKey(legEnd)).join('-');
  }

  static toNodeIds(planUrlString: string): List<string> {
    const nodeIdsRadix36: List<string> = List(planUrlString.split('-'));
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
    return PlanUtil.legEndKey(source) + '>' + PlanUtil.legEndKey(sink);
  }

  static legEndNode(nodeId: number): LegEnd {
    return new LegEnd(new LegEndNode(nodeId), null);
  }

  static legEndRoute(trackPathKeys: List<TrackPathKey>): LegEnd {
    return new LegEnd(null, new LegEndRoute(trackPathKeys, null));
  }

  static legEndRoutes(routeFeatures: List<RouteFeature>): LegEnd {
    const trackPathKeys = routeFeatures.map(routeFeature => routeFeature.toTrackPathKey());
    return new LegEnd(null, new LegEndRoute(trackPathKeys, null));
  }

  static legEndKey(legEnd: LegEnd): string {
    if (legEnd.node) {
      return legEnd.node.nodeId.toString();
    }
    if (legEnd.route) {
      return legEnd.route.trackPathKeys.map(trackPath => `${trackPath.routeId}.${trackPath.pathId}`).join('|');
    }
    return '';
  }

  static encodedLegEndKey(legEnd: LegEnd): string {
    if (legEnd.node) {
      return legEnd.node.nodeId.toString(36);
    }
    if (legEnd.route?.selection) {
      const trackPath = legEnd.route.selection;
      const routeId = trackPath.routeId.toString(36);
      const pathId = trackPath.pathId.toString(36);
      return `${routeId}.${pathId}`;
    }
    if (legEnd.route) {
      return legEnd.route.trackPathKeys.map(trackPath => {
        const routeId = trackPath.routeId.toString(36);
        const pathId = trackPath.pathId.toString(36);
        return `${routeId}.${pathId}`;
      }).join('|');
    }
    return '';
  }

  static startFlag(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.Start, FeatureId.next(), coordinate);
  }

  static viaFlag(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.Via, FeatureId.next(), coordinate);
  }

  static endFlag(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.End, FeatureId.next(), coordinate);
  }

  static invisibleFlag(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.Invisible, FeatureId.next(), coordinate);
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

  static planRouteLatLons(planRoute: PlanRoute): List<LatLonImpl> {
    return List([planRoute.sourceNode.latLon])
      .concat(planRoute.segments.flatMap(segment => segment.fragments.map(fragment => fragment.latLon)));
  }

  static planRouteCoordinates(planRoute: PlanRoute): List<Coordinate> {
    return List([planRoute.sourceNode.coordinate])
      .concat(planRoute.segments.flatMap(segment => segment.fragments.map(fragment => fragment.coordinate)));
  }

  static singleRoutePlanLeg(featureId: string, sourceNode: PlanNode, sinkNode: PlanNode, sinkFlag: PlanFlag, viaFlag: PlanFlag): PlanLeg {

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const legKey = sourceNode.nodeId + '-' + sinkNode.nodeId;

    const fragment = new PlanFragment(0, 0, -1, sinkNode.coordinate, sinkNode.latLon);
    const segment = new PlanSegment(0, '', null, List([fragment]));
    const route = new PlanRoute(sourceNode, sinkNode, 0, List([segment]), List());
    return new PlanLeg(featureId, legKey, source, sink, sinkFlag, viaFlag, List([route]));
  }

  static leg(data: PlanLegData, sinkFlag: PlanFlag, viaFlag: PlanFlag): PlanLeg {
    const legKey = PlanUtil.key(data.source, data.sink);
    return new PlanLeg(FeatureId.next(), legKey, data.source, data.sink, sinkFlag, viaFlag, data.routes);
  }

  static planRoute(sourceNode: PlanNode, sinkNode: PlanNode): PlanRoute {
    const fragment = new PlanFragment(0, 0, -1, sinkNode.coordinate, sinkNode.latLon);
    const segment = new PlanSegment(0, '', null, List([fragment]));
    return new PlanRoute(sourceNode, sinkNode, 0, List([segment]), List());
  }

}
