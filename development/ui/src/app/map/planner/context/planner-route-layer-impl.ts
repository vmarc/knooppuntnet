import {List} from "immutable";
import Coordinate from 'ol/coordinate';
import Feature from 'ol/Feature';
import {LineString, Point} from 'ol/geom.js';
import {Vector as VectorLayer} from 'ol/layer';
import Map from 'ol/Map';
import {Vector as VectorSource} from 'ol/source';
import {Icon, Stroke, Style} from 'ol/style';
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

  addStartNodeFlag(nodeId: string, coordinate: Coordinate): void {
    return this.addNodeFlag(this.startNodeKey(nodeId), nodeId, coordinate, this.startFlagStyle);
  }

  addViaNodeFlag(legId: string, nodeId: string, coordinate: Coordinate): void {
    return this.addNodeFlag(this.viaNodeKey(legId, nodeId), nodeId, coordinate, this.viaFlagStyle);
  }

  removeStartNodeFlag(nodeId: string): void {
    this.removeNodeFlag(this.startNodeKey(nodeId));
  }

  removeViaNodeFlag(legId: string, nodeId: string): void {
    this.removeNodeFlag(this.viaNodeKey(legId, nodeId));
  }

  updateFlagPosition(featureId: string, coordinate: Coordinate): void {
    const feature = this.source.getFeatureById(featureId);
    if (feature) {
      feature.getGeometry().setCoordinates(coordinate);
    }
  }

  addRouteLeg(legId: string, coordinates: List<Coordinate>): void {
    this.removeRouteLeg(legId);
    const feature = new Feature(new LineString(coordinates.toArray()));
    feature.setId(legId);
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

  private addNodeFlag(id: string, nodeId: string, coordinate: Coordinate, style: Style): void {
    const feature = new Feature(new Point(coordinate));
    feature.setId(id);
    feature.set("layer", "leg-node");
    feature.set("nodeId", nodeId);
    feature.setStyle(style);
    this.source.addFeature(feature);
  }

  private removeNodeFlag(id: string) {
    const feature = this.source.getFeatureById(id);
    this.source.removeFeature(feature);
  }

  private startNodeKey(nodeId: string) {
    return "start-node-flag-" + nodeId;
  }

  private viaNodeKey(legId: string, nodeId: string) {
    return "leg-" + legId + "-via-node-" + nodeId;
  }

}
