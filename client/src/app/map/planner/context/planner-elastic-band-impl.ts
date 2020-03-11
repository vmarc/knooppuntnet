import {Coordinate} from "ol/coordinate";
import Feature from "ol/Feature";
import LineString from "ol/geom/LineString";
import VectorLayer from "ol/layer/Vector";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import {PlannerElasticBand} from "./planner-elastic-band";

export class PlannerElasticBandImpl implements PlannerElasticBand {

  private rubberBandStyle = new Style({
    stroke: new Stroke({
      color: "rgba(0, 0, 255, 0.7)",
      lineDash: [10, 10],
      width: 2
    })
  });

  anchor1: Coordinate;
  anchor2: Coordinate;

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
    this.layer.setVisible(false);
  }

  addToMap(map: Map) {
    map.addLayer(this.layer);
  }

  set(anchor1: Coordinate, anchor2: Coordinate, position: Coordinate) {
    this.anchor1 = anchor1;
    this.anchor2 = anchor2;
    this.updatePosition(position);
    this.layer.setVisible(true);
  }

  setInvisible() {
    this.layer.setVisible(false);
  }

  updatePosition(position: Coordinate) {
    this.line1.setCoordinates([this.anchor1, position]);
    this.line2.setCoordinates([this.anchor2, position]);
  }

}
