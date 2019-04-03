import Map from 'ol/Map';
import Coordinate from 'ol/coordinate';
import MapBrowserEvent from 'ol/events'
import PointerInteraction from 'ol/interaction/Pointer';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import {PlannerEngine} from "./planner-engine";
import {PlannerContext} from "./planner-context";
import {PlannerMapFeature} from "./features/planner-map-feature";
import {List} from "immutable";
import {PlannerMapFeatureLeg} from "./features/planner-map-feature-leg";
import {PlannerMapFeatureLegNode} from "./features/planner-map-feature-leg-node";
import {PlannerMapFeatureNetworkNode} from "./features/planner-map-feature-network-node";

export class PlannerInteraction {

  constructor(private context: PlannerContext,
              private engine: PlannerEngine) {
  }

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

  addToMap(map: Map) {
    map.addInteraction(this.interaction);
    map.getViewport().addEventListener("mouseout", (e) => this.engine.handleMouseOut());
    map.getViewport().addEventListener("mouseenter", (e) => this.engine.handleMouseEnter());
  }

  private getFeaturesAt(evt: MapBrowserEvent): List<PlannerMapFeature> {
    const tolerance = 20;
    const features = evt.map.getFeaturesAtPixel(evt.pixel, tolerance);
    if (features) {
      return List(features.map(feature => this.fromFeature(feature)).filter(f => f !== null));
    }
    return List();
  }

  private fromFeature(feature: Feature): PlannerMapFeature {

    const layer = feature.get("layer");
    if (layer) {
      if ("leg" === layer) {
        const legId = feature.getId();
        return new PlannerMapFeatureLeg(legId);
      }
      if ("leg-node" === layer) {
        const id = feature.getId();
        const nodeId = feature.get("id");
        return new PlannerMapFeatureLegNode(id, nodeId);
      }
      if (layer.endsWith("node")) {
        const nodeId = feature.get("id");
        const nodeName = feature.get("name");
        const point: Point = feature.getGeometry() as Point;
        const coordinate: Coordinate = point.getCoordinates();
        return new PlannerMapFeatureNetworkNode(nodeId, nodeName, coordinate);
      }
    }

    // we are not interested in the feature for planner purposes
    return null;
  }

}
