import { MapGeoJSONFeature } from 'maplibre-gl';
import { FlagFeature } from '../features/flag-feature';
import { LegFeature } from '../features/leg-feature';
import { MapFeature } from '../features/map-feature';
import { NetworkNodeFeature } from '../features/network-node-feature';
import { PoiFeature } from '../features/poi-feature';
import { RouteFeature } from '../features/route-feature';

export class Features {
  static findFlag(features: MapFeature[]): FlagFeature | undefined {
    return features.find((f): f is FlagFeature => f instanceof FlagFeature);
  }

  static findNetworkNode(features: ReadonlyArray<MapFeature>): NetworkNodeFeature | undefined {
    return features.find((f): f is NetworkNodeFeature => f instanceof NetworkNodeFeature);
  }

  static findLeg(features: ReadonlyArray<MapFeature>): LegFeature | undefined {
    return features.find((f): f is LegFeature => f instanceof LegFeature);
  }

  static findPoi(features: ReadonlyArray<MapFeature>): PoiFeature | undefined {
    return features.find((f): f is PoiFeature => f instanceof PoiFeature);
  }

  static findRoute(features: ReadonlyArray<MapFeature>): RouteFeature | undefined {
    return features.find((f): f is RouteFeature => f instanceof RouteFeature);
  }

  static findRoutes(features: ReadonlyArray<MapFeature>): RouteFeature[] {
    return features.filter((f): f is RouteFeature => f instanceof RouteFeature);
  }

  static mapFeature(feature: MapGeoJSONFeature): MapFeature | undefined {
    const layerId = feature.layer.id;

    // console.log('mapFeature', layerId, feature);

    // if ('leg' === featureLayer) {
    //   const legId = feature.id as string;
    //   return new LegFeature(legId);
    // }

    // if ('flag' === featureLayer) {
    //   const id = feature.getId() as string;
    //   const flagType = feature.get('flag-type');
    //   return new FlagFeature(flagType, id);
    // }

    if (layerId == 'route-hiking-node') {
      const nodeId = feature.properties['id'];
      const ref = feature.properties['ref'];
      if (feature.geometry.type == 'Point') {
        const coordinate = feature.geometry.coordinates;
        return NetworkNodeFeature.create(nodeId, ref, '', coordinate, false /*TODO planner*/);
      }
    }

    // if (layerId.endsWith('node') && !layerId.endsWith('opendata-node')) {
    //   const nodeId = feature.id;
    //   const proposed = feature.get('state') === 'proposed';
    //   let nodeRef = feature.get('ref');
    //   const nodeName = feature.get('name');
    //   if (nodeName && nodeRef === 'o') {
    //     nodeRef = undefined;
    //   }
    //   let name = nodeRef;
    //   if (!name) {
    //     name = nodeName;
    //   }
    //
    //   let nodeLongName = undefined;
    //   if (nodeName && nodeRef) {
    //     nodeLongName = nodeName;
    //   }
    //
    //   const point: Point = feature.getGeometry() as Point;
    //   const extent = point.getExtent();
    //   const coordinate: Coordinate = [extent[0], extent[1]];
    //   return NetworkNodeFeature.create(nodeId, name, nodeLongName, coordinate, proposed);
    // }

    // const layerType = feature.get('type');
    // if ('node' === layerType || 'way' === layerType || 'relation' === layerType) {
    //   const poiId = feature.get('id');
    //   const point: Point = feature.getGeometry() as Point;
    //   const extent = point.getExtent();
    //   const coordinate: Coordinate = [extent[0], extent[1]];
    //   return new PoiFeature(poiId, layerType, featureLayer, coordinate);
    // }

    if (layerId == 'route-hiking-node-route') {
      const routeId = feature.properties['routeId'];
      const segmentId = feature.properties['segmentId'];
      const routeName = feature.properties['name'];
      const oneWay = feature.properties['oneway'] === 'true';
      const proposed = feature.properties['state'] === 'proposed';
      return new RouteFeature(+routeId, +segmentId, routeName, oneWay, proposed, feature);
    }

    // if (featureLayer.endsWith('route') && !featureLayer.endsWith('opendata-route')) {
    //   const segmentId = feature.get('id');
    //   const routeName = feature.get('name');
    //   const oneWay = feature.get('oneway') === 'true';
    //   let dashIndex = -1;
    //   let routeId = 0;
    //   let pathId = 0;
    //   if (segmentId) {
    //     dashIndex = segmentId.indexOf('-');
    //     routeId = dashIndex === -1 ? segmentId : segmentId.substring(0, dashIndex);
    //     pathId = dashIndex === -1 ? -1 : segmentId.substring(dashIndex + 1);
    //   }
    //   const proposed = feature.get('state') === 'proposed';
    //   return new RouteFeature(+routeId, +pathId, routeName, oneWay, proposed, feature);
    // }

    // we are not interested in the feature for planner purposes
    return undefined;
  }
}
