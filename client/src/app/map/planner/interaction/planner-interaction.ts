import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {platformModifierKeyOnly} from "ol/events/condition";
import {FeatureLike} from "ol/Feature";
import Point from "ol/geom/Point";
import PointerInteraction from "ol/interaction/Pointer";
import Map from "ol/Map";
import MapBrowserEvent from "ol/MapBrowserEvent";
import {FlagFeature} from "../features/flag-feature";
import {LegFeature} from "../features/leg-feature";
import {MapFeature} from "../features/map-feature";
import {NetworkNodeFeature} from "../features/network-node-feature";
import {NodeFeature} from "../features/node-feature";
import {PoiFeature} from "../features/poi-feature";
import {RouteFeature} from "../features/route-feature";
import {PlannerEngine} from "./planner-engine";

export class PlannerInteraction {

  private downEventTime = 0;

  private interaction = new PointerInteraction({
    handleDownEvent: (evt: MapBrowserEvent) => {
      this.downEventTime = new Date().getTime();
      return this.engine.handleDownEvent(this.getFeaturesAt(evt), evt.coordinate);
    },
    handleMoveEvent: (evt: MapBrowserEvent) => {
      return this.engine.handleMoveEvent(this.getFeaturesAt(evt), evt.coordinate);
    },
    handleDragEvent: (evt: MapBrowserEvent) => {
      return this.engine.handleDragEvent(this.getFeaturesAt(evt), evt.coordinate);
    },
    handleUpEvent: (evt: MapBrowserEvent) => {
      const upEventTime = new Date().getTime();
      const timeSinceUp = upEventTime - this.downEventTime;
      const singleClick = timeSinceUp < 800;
      return this.engine.handleUpEvent(this.getFeaturesAt(evt), evt.coordinate, singleClick);
    }
  });

  constructor(private engine: PlannerEngine) {
  }

  private static mapFeature(feature: FeatureLike): MapFeature {

    const layer = feature.get("layer");
    if (layer) {
      if ("leg" === layer) {
        const legId = feature.getId() as string;
        return new LegFeature(legId);
      }
      if ("flag" === layer) {
        const id = feature.getId() as string;
        const flagType = feature.get("flag-type");
        return new FlagFeature(flagType, id);
      }
      if (layer.endsWith("node")) {
        const nodeId = feature.get("id");
        const nodeName = feature.get("name");
        const point: Point = feature.getGeometry() as Point;
        const extent = point.getExtent();
        const coordinate: Coordinate = [extent[0], extent[1]];
        return NetworkNodeFeature.create(nodeId, nodeName, coordinate);
      }

      const layerType = feature.get("type");
      if ("node" === layerType || "way" === layerType || "relation" === layerType) {
        const poiId = feature.get("id");
        const point: Point = feature.getGeometry() as Point;
        const extent = point.getExtent();
        const coordinate: Coordinate = [extent[0], extent[1]];
        return new PoiFeature(poiId, layerType, layer, coordinate);
      }

      if (layer.endsWith("route")) {
        const segmentId = feature.get("id");
        const routeName = feature.get("name");
        const dashIndex = segmentId.indexOf("-");
        const routeId = dashIndex === -1 ? segmentId : segmentId.substr(0, dashIndex);
        return new RouteFeature(+routeId, routeName, feature);
      }
    }

    // we are not interested in the feature for planner purposes
    return null;
  }

  private static analysisFeature(feature: FeatureLike): MapFeature {

    const layer = feature.get("layer");
    if (layer) {
      if (layer.endsWith("node")) {
        const nodeId = feature.get("id");
        const nodeName = feature.get("name");
        return new NodeFeature(+nodeId, nodeName);
      }
      if (layer.endsWith("route")) {
        const segmentId = feature.get("id");
        const routeName = feature.get("name");
        const dashIndex = segmentId.indexOf("-");
        const routeId = dashIndex === -1 ? segmentId : segmentId.substr(0, dashIndex);
        return new RouteFeature(+routeId, routeName, feature);
      }
    }

    // we are not interested in the feature for analysis purposes
    return null;
  }

  addToMap(map: Map) {
    map.addInteraction(this.interaction);
  }

  private getFeaturesAt(evt: MapBrowserEvent): List<MapFeature> {
    const features = evt.map.getFeaturesAtPixel(evt.pixel);
    if (features) {
      return List(
        features.map(feature => {
          if (platformModifierKeyOnly(evt)) {
            return PlannerInteraction.analysisFeature(feature);
          }
          return PlannerInteraction.mapFeature(feature);
        }).filter(f => f !== null)
      );
    }
    return List();
  }

}
