import Coordinate from 'ol/coordinate';
import Feature from 'ol/Feature';
import {LineString, Point} from 'ol/geom.js';
import {Vector as VectorLayer} from 'ol/layer';
import Map from 'ol/Map';
import {Vector as VectorSource} from 'ol/source';
import {Icon, Stroke, Style} from 'ol/style';
import {PlanLeg} from "../plan/plan-leg";
import {PlannerRouteLayer} from "./planner-route-layer";

/*
  - displays planned route
  - displays flags for the nodes in the route plan
 */
export class PlannerRouteLayerImpl implements PlannerRouteLayer {

  private startFlagStyle = new Style({
    image: new Icon({
      anchor: [12, 41],
      anchorXUnits: "pixels",
      anchorYUnits: "pixels",
      src: "/assets/images/marker-icon-green.png"
    })
  });

  private viaFlagStyle = new Style({
    image: new Icon({
      anchor: [12, 41],
      anchorXUnits: "pixels",
      anchorYUnits: "pixels",
      src: "/assets/images/marker-icon-orange.png"
    })
  });

  private legStyle = new Style({
    stroke: new Stroke({
      color: "rgba(255, 0, 255, 0.5)",
      width: 12
    })
  });

  private source = new VectorSource();

  private layer = new VectorLayer({
    source: this.source
  });

  addToMap(map: Map) {
    map.addLayer(this.layer);
  }

  addFlag(flag: PlanFlag): void {
    const feature = new Feature(new Point(flag.coordinate));
    feature.setId(flag.featureId);
    feature.set("layer", "flag");
    feature.set("flag-type", flag.flagType);
    if (flag.flagType == PlanFlagType.Start) {
      feature.setStyle(this.startFlagStyle);
    } else if (flag.flagType == PlanFlagType.Via) {
      feature.setStyle(this.viaFlagStyle);
    }
    // feature.set("nodeId", nodeId); // TODO do we need this???
    this.source.addFeature(feature);
  }

  removeFlag(featureId: string): void {
    const feature = this.source.getFeatureById(featureId);
    this.source.removeFeature(feature);
  }

  updateFlagCoordinate(featureId: string, coordinate: Coordinate): void {
    const feature = this.source.getFeatureById(featureId);
    if (feature) {
      feature.getGeometry().setCoordinates(coordinate);
    }
  }

  addRouteLeg(leg: PlanLeg): void {
    this.removeRouteLeg(leg.featureId);
    const feature = new Feature(new LineString(leg.coordinates().toArray()));
    feature.setId(leg.featureId);
    feature.set("layer", "leg");
    feature.setStyle(this.legStyle);
    this.source.addFeature(feature);
  }

  removeRouteLeg(legId: string): void {
    const feature = this.source.getFeatureById(legId);
    if (feature) {
      this.source.removeFeature(feature);
    }
  }

  private addNodeFlag(id: string, nodeId: string, coordinate: Coordinate, flagType: string, style: Style): void {
    const feature = new Feature(new Point(coordinate));
    feature.setId(id);
    feature.set("layer", "flag");
    feature.set("flag-type", flagType);
    feature.set("nodeId", nodeId);
    feature.setStyle(style);
    this.source.addFeature(feature);
  }

  private removeNodeFlag(id: string) {
    const feature = this.source.getFeatureById(id);
    this.source.removeFeature(feature);
  }

}
