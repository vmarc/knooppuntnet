import { Bounds } from '@api/common/bounds';
import { TrackPathKey } from '@api/common/common/track-path-key';
import { LatLonImpl } from '@api/common/lat-lon-impl';
import { LegEnd } from '@api/common/planner/leg-end';
import { PlanFragment } from '@api/common/planner/plan-fragment';
import { PlanNode } from '@api/common/planner/plan-node';
import { PlanRoute } from '@api/common/planner/plan-route';
import { PlanSegment } from '@api/common/planner/plan-segment';
import { Coordinate } from '@api/custom/coordinate';
import { OlUtil } from '@app/ol/ol-util';
import { PlanLegData } from '../context/plan-leg-data';
import { FeatureId } from '../features/feature-id';
import { RouteFeature } from '../features/route-feature';
import { Plan } from './plan';
import { PlanFlag } from './plan-flag';
import { PlanFlagType } from './plan-flag-type';
import { PlanLeg } from './plan-leg';

export class PlanUtil {
  static toUrlString(plan: Plan): string {
    const legEnds: LegEnd[] = [];

    if (plan.sourceNode) {
      legEnds.push(PlanUtil.legEndNode(+plan.sourceNode.nodeId));
    }

    plan.legs.forEach((leg) => {
      legEnds.push(leg.sink);
    });

    return legEnds.map((legEnd) => PlanUtil.encodedLegEndKey(legEnd)).join('-');
  }

  static toNodeIds(planUrlString: string): ReadonlyArray<string> {
    const nodeIdsRadix36: string[] = planUrlString.split('-');
    return nodeIdsRadix36.map((nodeId) => parseInt(nodeId, 36).toString());
  }

  static distinctColours(colours: string[]): string[] {
    return colours.reduce((unique: Array<string>, colour): Array<string> => {
      if (unique.length === 0 || unique.at(-1) !== colour) {
        unique.push(colour);
        return unique;
      }
      return unique;
    }, Array<string>());
  }

  static key(source: LegEnd, sink: LegEnd): string {
    return PlanUtil.legEndKey(source) + '>' + PlanUtil.legEndKey(sink);
  }

  static legEndNode(nodeId: number): LegEnd {
    return {
      node: {
        nodeId,
      },
    };
  }

  static legEndRoute(trackPathKeys: ReadonlyArray<TrackPathKey>): LegEnd {
    return {
      route: {
        trackPathKeys,
        selection: null,
      },
    };
  }

  static legEndRoutes(routeFeatures: ReadonlyArray<RouteFeature>): LegEnd {
    const trackPathKeys = routeFeatures.map((routeFeature) => routeFeature.toTrackPathKey());
    return {
      node: null,
      route: {
        trackPathKeys,
        selection: null,
      },
    };
  }

  static legEndKey(legEnd: LegEnd): string {
    if (legEnd.node) {
      return legEnd.node.nodeId.toString();
    }
    if (legEnd.route) {
      return legEnd.route.trackPathKeys
        .map((trackPath) => `${trackPath.routeId}.${trackPath.pathId}`)
        .join('|');
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
      return legEnd.route.trackPathKeys
        .map((trackPath) => {
          const routeId = trackPath.routeId.toString(36);
          const pathId = trackPath.pathId.toString(36);
          return `${routeId}.${pathId}`;
        })
        .join('|');
    }
    return '';
  }

  static startFlag(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.start, FeatureId.next(), coordinate);
  }

  static viaFlag(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.via, FeatureId.next(), coordinate);
  }

  static endFlag(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.end, FeatureId.next(), coordinate);
  }

  static invisibleFlag(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.invisible, FeatureId.next(), coordinate);
  }

  static planNode(
    nodeId: string,
    nodeName: string,
    nodeLongName: string,
    latLon: LatLonImpl
  ): PlanNode {
    const coordinate = OlUtil.latLonToCoordinate(latLon);
    return {
      featureId: FeatureId.next(),
      nodeId,
      nodeName,
      nodeLongName,
      coordinate,
      latLon,
    };
  }

  static planNodeWithCoordinate(
    nodeId: string,
    nodeName: string,
    nodeLongName: string,
    coordinate: Coordinate
  ): PlanNode {
    const latLon = OlUtil.latLonFromCoordinate(coordinate);
    return {
      featureId: FeatureId.next(),
      nodeId,
      nodeName,
      nodeLongName,
      coordinate,
      latLon,
    };
  }

  static planSinkNode(plan: Plan): PlanNode {
    const lastLeg = plan.legs.at(-1);
    if (lastLeg) {
      return lastLeg.sinkNode;
    }
    return plan.sourceNode;
  }

  static planRouteLatLons(planRoute: PlanRoute): LatLonImpl[] {
    const latLons: Array<LatLonImpl> = [];
    latLons.push(planRoute.sourceNode.latLon);
    planRoute.segments.forEach((segment) =>
      segment.fragments.forEach((fragment) => latLons.push(fragment.latLon))
    );
    return latLons;
  }

  static planBounds(plan: Plan): Bounds {
    if (plan.sourceNode === null) {
      return null;
    }
    const latLons: LatLonImpl[] = [plan.sourceNode.latLon].concat(
      plan.legs.flatMap((leg) =>
        [leg.sourceNode.latLon].concat(
          leg.routes.flatMap((route) => PlanUtil.planRouteLatLons(route))
        )
      )
    );

    const lats = latLons.map((latLon) => +latLon.latitude);
    const lons = latLons.map((latLon) => +latLon.longitude);
    const minLat = Math.min(...lats);
    const minLon = Math.min(...lons);
    const maxLat = Math.max(...lats);
    const maxLon = Math.max(...lons);
    return {
      minLat,
      minLon,
      maxLat,
      maxLon,
    };
  }

  static planRouteCoordinates(planRoute: PlanRoute): readonly Coordinate[] {
    const coordinates: Array<Coordinate> = [];
    coordinates.push(planRoute.sourceNode.coordinate);
    planRoute.segments.forEach((segment) =>
      segment.fragments.forEach((fragment) => coordinates.push(fragment.coordinate))
    );
    return coordinates;
  }

  static singleRoutePlanLeg(
    featureId: string,
    sourceNode: PlanNode,
    sinkNode: PlanNode,
    sinkFlag: PlanFlag,
    viaFlag: PlanFlag
  ): PlanLeg {
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const legKey = sourceNode.nodeId + '-' + sinkNode.nodeId;
    const fragment: PlanFragment = {
      meters: 0,
      coordinate: sinkNode.coordinate,
      latLon: sinkNode.latLon,
    };
    const segment: PlanSegment = {
      meters: 0,
      surface: '',
      colour: null,
      fragments: [fragment],
    };
    const route: PlanRoute = {
      sourceNode,
      sinkNode,
      meters: 0,
      segments: [segment],
    };
    return new PlanLeg(featureId, legKey, source, sink, sinkFlag, viaFlag, [route]);
  }

  static leg(data: PlanLegData, sinkFlag: PlanFlag, viaFlag: PlanFlag): PlanLeg {
    const legKey = PlanUtil.key(data.source, data.sink);
    return new PlanLeg(
      FeatureId.next(),
      legKey,
      data.source,
      data.sink,
      sinkFlag,
      viaFlag,
      data.routes
    );
  }

  static planRoute(sourceNode: PlanNode, sinkNode: PlanNode): PlanRoute {
    const fragment: PlanFragment = {
      meters: 0,
      coordinate: sinkNode.coordinate,
      latLon: sinkNode.latLon,
    };
    const segment: PlanSegment = {
      meters: 0,
      surface: '',
      colour: null,
      fragments: [fragment],
    };
    return {
      sourceNode,
      sinkNode,
      meters: 0,
      segments: [segment],
    };
  }
}
