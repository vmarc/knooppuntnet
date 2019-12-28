import Map from "ol/Map";
import MapBrowserEvent from "ol/events"
import PointerInteraction from "ol/interaction/Pointer";
import Coordinate from "ol/coordinate";
import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import {List} from "immutable";
import {MapFeature} from "../features/map-feature";
import {PlannerEngine} from "./planner-engine";
import {LegFeature} from "../features/leg-feature";
import {FlagFeature} from "../features/flag-feature";
import {NetworkNodeFeature} from "../features/network-node-feature";
import {PoiFeature} from "../features/poi-feature";

export class PlannerInteraction {

  private interaction = new PointerInteraction({
    handleDownEvent: (evt: MapBrowserEvent) => {
      return this.engine.handleDownEvent(this.getFeaturesAt(evt), evt.coordinate);
    },
    handleMoveEvent: (evt: MapBrowserEvent) => {
      return this.engine.handleMoveEvent(this.getFeaturesAt(evt), evt.coordinate);
    },
    handleDragEvent: (evt: MapBrowserEvent) => {
      return this.engine.handleDragEvent(this.getFeaturesAt(evt), evt.coordinate);
    },
    handleUpEvent: (evt: MapBrowserEvent) => {
      return this.engine.handleUpEvent(this.getFeaturesAt(evt), evt.coordinate);
    }
  });

  constructor(private engine: PlannerEngine) {
  }

  private static mapFeature(feature: Feature): MapFeature {

    const layer = feature.get("layer");
    if (layer) {
      if ("leg" === layer) {
        const legId = feature.getId();
        return new LegFeature(legId);
      }
      if ("flag" === layer) {
        const id = feature.getId();
        const flagType = feature.get("flag-type");
        return new FlagFeature(id, flagType);
      }
      if (layer.endsWith("node")) {
        const nodeId = feature.get("id");
        const nodeName = feature.get("name");
        const point: Point = feature.getGeometry() as Point;
        const coordinate: Coordinate = point.getCoordinates();
        return NetworkNodeFeature.create(nodeId, nodeName, coordinate);
      }

      const layerType = feature.get("type");
      if ("node" == layerType || "way" == layerType || "relation" == layerType) {
        const poiId = feature.get("id");
        const point: Point = feature.getGeometry() as Point;
        const coordinate: Coordinate = point.getCoordinates();
        return new PoiFeature(poiId, layerType, layer, coordinate);
      }
    }

    // we are not interested in the feature for planner purposes
    return null;
  }

  addToMap(map: Map) {
    map.addInteraction(this.interaction);
  }

  private getFeaturesAt(evt: MapBrowserEvent): List<MapFeature> {
    const tolerance = 20;
    const features = evt.map.getFeaturesAtPixel(evt.pixel, tolerance);
    if (features) {
      return List(features.map(feature => PlannerInteraction.mapFeature(feature)).filter(f => f !== null));
    }
    return List();
  }

}
