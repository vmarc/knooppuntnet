import {Coordinate} from "ol/coordinate";
import Feature from "ol/Feature";
import LineString from "ol/geom/LineString";
import VectorLayer from "ol/layer/Vector";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import {Marker} from "../../../components/ol/domain/marker";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerRouteLayer} from "./planner-route-layer";
import Point from "ol/geom/Point";

/*
  - displays planned route
  - displays flags for the nodes in the route plan
 */
export class PlannerRouteLayerImpl implements PlannerRouteLayer {

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
    let markerColor = "blue";
    if (flag.flagType === PlanFlagType.End) {
      markerColor = "green";
    } else if (flag.flagType === PlanFlagType.Via) {
      markerColor = "orange";
    }
    const marker = Marker.create(markerColor, flag.coordinate);
    marker.setId(flag.featureId);
    marker.set("layer", "flag");
    marker.set("flag-type", flag.flagType);
    this.source.addFeature(marker);
  }

  removeFlag(featureId: string): void {
    const feature = this.source.getFeatureById(featureId);
    if (feature != null) {
      this.source.removeFeature(feature);
    }
  }

  updateFlagCoordinate(featureId: string, coordinate: Coordinate): void {
    const feature = this.source.getFeatureById(featureId);
    if (feature) {
      (feature.getGeometry() as Point).setCoordinates(coordinate);
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

}
