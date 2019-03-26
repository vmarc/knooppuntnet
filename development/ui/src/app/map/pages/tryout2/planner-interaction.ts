import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';
import {click, pointerMove} from 'ol/events/condition';
import MapBrowserEvent from 'ol/events'
import {Fill, Icon, Stroke, Style} from 'ol/style';
import PointerInteraction from 'ol/interaction/Pointer';
import {OSM} from 'ol/source';
import {PlannerCrosshairLayer} from "./planner-crosshair-layer";
import {PlannerRouteLayer} from "./planner-route-layer";
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';

export class PlannerInteraction {

  constructor(private crosshairLayer: PlannerCrosshairLayer,
              private routeLayer: PlannerRouteLayer) {
  }

  public interaction = new PointerInteraction({
    handleDownEvent: (evt: MapBrowserEvent) => {

      const tolerance = 20;
      const features = evt.map.getFeaturesAtPixel(evt.pixel, tolerance);
      if (features !== null) {
        const legNode: Feature = this.getClosestLegNode(features);
        if (legNode !== null) {
          console.log("select feature node " + legNode.get("name"));
          const point: Point = legNode.getGeometry() as Point;
          this.routeLayer.addStartNodeFlag(legNode.get("name"), point.getCoordinates());
          this.crosshairLayer.updatePosition(point.getCoordinates());
        }
        else {
          this.crosshairLayer.updatePosition(evt.coordinate);
        }
      }
      else {
        this.crosshairLayer.updatePosition(evt.coordinate);
      }

      // this.crosshairLayer.updatePosition(evt.coordinate);
      // this.routeLayer.updateDoubleElasticBandPosition(evt.coordinate);
      return false;
    },
    handleMoveEvent: evt => {
      const tolerance = 20;
      const features = evt.map.getFeaturesAtPixel(evt.pixel, tolerance);
      if (features !== null) {
        const legNode: Feature = this.getClosestLegNode(features);
        if (legNode !== null) {
          const point: Point = legNode.getGeometry() as Point;
          this.crosshairLayer.updatePosition(point.getCoordinates());
        }
        else {
          this.crosshairLayer.updatePosition(evt.coordinate);
        }
      }
      else {
        this.crosshairLayer.updatePosition(evt.coordinate);
      }

      // evt.map.forEachFeatureAtPixel(evt.pixel, feature => {
      //   if (feature) {
      //
      //     const layer = feature.get("layer");
      //     if (layer) {
      //       if (layer == "leg") {
      //         console.log("feature leg");
      //       } else if (layer == "leg-node") {
      //         console.log("feature leg-node " + feature.getId());
      //       } else if (layer.endsWith("route")) {
      //         console.log("feature route " + feature.get("id"));
      //       } else if (layer.endsWith("node")) {
      //         console.log("feature node " + feature.get("name"));
      //       } else {
      //         console.log("other feature, layer = " + layer);
      //       }
      //     } else {
      //       console.log("other feature ");
      //     }
      //   }
      // });

      return true;
    },
    handleDragEvent: evt => {
      this.crosshairLayer.updatePosition(evt.coordinate);
      this.routeLayer.updateDoubleElasticBandPosition(evt.coordinate);
      return true;
    },
    handleUpEvent: evt => {
      return false;
    }
  });

  private getClosestLegNode(features: Array<Feature>): Feature {
    const legNodes = features.filter(f => {
      const layer = f.get("layer");
      return layer && layer.endsWith("node") && layer !== "leg-node";
    });
    if (legNodes.length == 0) {
      return null;
    }
    return legNodes[0]; // TODO find the closest
  }

}
