import Map from 'ol/Map';
import Coordinate from 'ol/View';
import {fromLonLat} from 'ol/proj';
import {Icon, Stroke, Style} from 'ol/style';
import Feature from 'ol/Feature';
import {LineString, Point} from 'ol/geom.js';
import {Vector as VectorLayer} from 'ol/layer';
import {Vector as VectorSource} from 'ol/source';
import {List} from "immutable";

/*
  - displays planned route
  - displays flags for the nodes in the route plan
 */
export class PlannerRouteLayer {

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

  private endFlagStyle = new Style({
    image: new Icon({
      anchor: [12, 41],
      anchorXUnits: "pixels",
      anchorYUnits: "pixels",
      src: "/assets/images/marker-icon-red.png"
    })
  });

  private legStyle = new Style({
    stroke: new Stroke({
      color: "rgba(255, 0, 255, 0.5)",
      width: 12
    })
  });

  private rubberBandStyle = new Style({
    stroke: new Stroke({
      color: "rgba(0, 0, 255, 0.7)",
      lineDash: [10, 10],
      width: 2
    })
  });

  coordinates1 = fromLonLat([4.4484, 51.4627]); // Hemelrijk
  coordinates2 = fromLonLat([4.4835, 51.4748]); // Steenpaal

  private line1 = new LineString([[0, 0], [0, 0]]);
  private line2 = new LineString([[0, 0], [0, 0]]);
  private line1Feature = new Feature(this.line1);
  private line2Feature = new Feature(this.line2);

  private source = new VectorSource({
    features: [this.line1Feature, this.line2Feature]
  });

  private layer = new VectorLayer({
    source: this.source
  });

  constructor() {
    this.line1Feature.setStyle(this.rubberBandStyle);
    this.line2Feature.setStyle(this.rubberBandStyle);
    // - add double elastic band features / hidden
    // - add single elastic band features / hidden (kan 1 van de 2 van double elastic band zijn?)
    // - setVisible(visible)
    // - setZIndex(zindex)
  }

  public addToMap(map: Map) {
    map.addLayer(this.layer);
  }

  public addStartNodeFlag(nodeId: string, coordinate: Coordinate): Feature {
    return this.addNodeFlag(this.startNodeKey(nodeId), nodeId, coordinate, this.startFlagStyle);
  }

  public addEndNodeFlag(nodeId: string, coordinate: Coordinate): Feature {
    return this.addNodeFlag(this.endNodeKey(nodeId), nodeId, coordinate, this.endFlagStyle);
  }

  public addViaNodeFlag(legId: string, nodeId: string, coordinate: Coordinate): Feature {
    return this.addNodeFlag(this.viaNodeKey(legId, nodeId), nodeId, coordinate, this.viaFlagStyle);
  }

  public removeStartNodeFlag(nodeId: string) {
    this.removeNodeFlag(this.startNodeKey(nodeId));
  }

  public removeEndNodeFlag(nodeId: string) {
    this.removeNodeFlag(this.endNodeKey(nodeId));
  }

  public removeViaNodeFlag(legId: string, nodeId: string) {
    this.removeNodeFlag(this.viaNodeKey(legId, nodeId));
  }

  private addNodeFlag(id: string, nodeId: string, coordinate: Coordinate, style: Style): Feature {
    const feature = new Feature(new Point(coordinate));
    feature.setId(id);
    feature.set("layer", "leg-node");
    feature.set("nodeId", nodeId);
    feature.setStyle(style);
    this.source.addFeature(feature);
    return feature;
  }

  private removeNodeFlag(id: string) {
    const feature = this.source.getFeatureById(id);
    this.source.removeFeature(feature);
  }

  private startNodeKey(nodeId: string) {
    return "start-node-flag-" + nodeId;
  }

  private endNodeKey(nodeId: string) {
    return "end-node-flag-" + nodeId;
  }

  private viaNodeKey(legId: string, nodeId: string) {
    return "leg-" + legId + "-via-node-" + nodeId;
  }

  public addRouteLeg(legId: string, coordinates: List<Coordinate>) {
    const feature = new Feature(new LineString(coordinates.toArray()));
    feature.setId(legId);
    feature.set("layer", "leg");
    feature.setStyle(this.legStyle);
    this.source.addFeature(feature);
  }

  public removeRouteLeg(legId: string) {
    const feature = this.source.getFeatureById(legId);
    if (feature) {
      this.source.removeFeature(feature);
    }
  }

  showDoubleElasticBand(anchor1: Coordinate, anchor2: Coordinate, coordinate: Coordinate) {
    this.coordinates1 = anchor1;
    this.coordinates2 = anchor2;
    this.updateDoubleElasticBandPosition(coordinate);
  }

  hideDoubleElasticBand() {
    // TODO change to set separate elastic band layer to invisible
    this.coordinates1 = new Coordinate([0, 0]);
    this.coordinates2 = this.coordinates1;
    this.updateDoubleElasticBandPosition(this.coordinates1);
  }

  updateDoubleElasticBandPosition(coordinate: Coordinate) {
    this.line1.setCoordinates([this.coordinates1, coordinate]);
    this.line2.setCoordinates([this.coordinates2, coordinate]);
  }

  showSingleElasticBand(anchor: Coordinate, coordinate: Coordinate) {
  }

  updateSingleElasticBandPosition(coordinate: Coordinate) {
  }

  hideSingleElasticBand() {
  }

}
