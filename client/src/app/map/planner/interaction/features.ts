import {List} from "immutable";
import {FlagFeature} from "../features/flag-feature";
import {LegFeature} from "../features/leg-feature";
import {MapFeature} from "../features/map-feature";
import {NetworkNodeFeature} from "../features/network-node-feature";
import {NodeFeature} from "../features/node-feature";
import {PoiFeature} from "../features/poi-feature";
import {RouteFeature} from "../features/route-feature";

export class Features {

  static findFlag(features: List<MapFeature>): FlagFeature {
    const flagFeatures = features.filter(f => f instanceof FlagFeature);
    if (flagFeatures.isEmpty()) {
      return null;
    }
    return flagFeatures.get(0) as FlagFeature; // TODO find the closest
  }

  static findNetworkNode(features: List<MapFeature>): NetworkNodeFeature {
    const nodes = features.filter(f => f instanceof NetworkNodeFeature);
    if (nodes.isEmpty()) {
      return null;
    }
    return nodes.get(0) as NetworkNodeFeature; // TODO find the closest
  }

  static findLeg(features: List<MapFeature>): LegFeature {
    const legs = features.filter(f => f instanceof LegFeature);
    if (legs.isEmpty()) {
      return null;
    }
    return legs.get(0) as LegFeature; // TODO find the closest
  }

  static findPoi(features: List<MapFeature>): PoiFeature {
    const pois = features.filter(f => f instanceof PoiFeature);
    if (pois.isEmpty()) {
      return null;
    }
    return pois.get(0) as PoiFeature; // TODO find the closest
  }

  static findNode(features: List<MapFeature>): NodeFeature {
    const nodes = features.filter(f => f instanceof NodeFeature);
    if (nodes.isEmpty()) {
      return null;
    }
    return nodes.get(0) as NodeFeature; // TODO find the closest
  }

  static findRoute(features: List<MapFeature>): RouteFeature {
    const routes = features.filter(f => f instanceof RouteFeature);
    if (routes.isEmpty()) {
      return null;
    }
    return routes.get(0) as RouteFeature; // TODO find the closest
  }
}
