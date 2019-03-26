import Map from 'ol/Map';
import Coordinate from 'ol/View';
import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';
import {click, pointerMove} from 'ol/events/condition';
import {Fill, Icon, Stroke, Style} from 'ol/style';
import Feature from 'ol/Feature';
import {LineString, Point} from 'ol/geom.js';
import {Vector as VectorLayer} from 'ol/layer';
import {OSM, Vector as VectorSource} from 'ol/source';
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

  public addStartNodeFlag(nodeId: string, coordinate: Coordinate) {
    this.addNodeFlag(this.startNodeKey(nodeId), coordinate, this.startFlagStyle);
  }

  public addEndNodeFlag(nodeId: string, coordinate: Coordinate) {
    this.addNodeFlag(this.endNodeKey(nodeId), coordinate, this.endFlagStyle);
  }

  public addViaNodeFlag(nodeId: string, coordinate: Coordinate) {
    this.addNodeFlag(this.viaNodeKey(nodeId), coordinate, this.viaFlagStyle);
  }

  public removeStartNodeFlag(nodeId: string) {
    this.removeNodeFlag(this.startNodeKey(nodeId));
  }

  public removeEndNodeFlag(nodeId: string) {
    this.removeNodeFlag(this.endNodeKey(nodeId));
  }

  public removeViaNodeFlag(nodeId: string) {
    this.removeNodeFlag(this.viaNodeKey(nodeId));
  }

  private addNodeFlag(id: string, coordinate: Coordinate, style: Style) {
    const feature = new Feature(new Point(coordinate));
    feature.setId(id);
    feature.set("layer", "leg-node");
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

  private endNodeKey(nodeId: string) {
    return "end-node-flag-" + nodeId;
  }

  private viaNodeKey(nodeId: string) {
    return "via-node-flag-" + nodeId;
  }

  public addRouteLeg(/*routeLegId, start, end,*/ coordinates: List<Coordinate>) {
    const feature = new Feature(new LineString(coordinates.toArray()));
    feature.setStyle(this.legStyle);
    feature.set("layer", "leg");
    this.source.addFeature(feature);
    // - routeLegId niet nodig indien we direct aan het Feature object zelf kunnen geraken op het moment dat we het nodig hebben voor bijvoorbeeld remove
    //   (we kunnen routeLeg niet veilig identificeren aan de hand van de start en end nodes)
    // - add Feature for route leg
    // - begin and end coordinates for double band drag as feature attribute
    // - unique id for remove and for drag start
  }

  public removeRouteLeg(routeLegId) {
  }

  public showDoubleElasticBand(anchor1: Coordinate, anchor2: Coordinate, coordinate: Coordinate) {
  }

  public hideDoubleElasticBand() {
  }

  public updateDoubleElasticBandPosition(coordinate: Coordinate) {
    this.line1.setCoordinates([this.coordinates1, coordinate]);
    this.line2.setCoordinates([this.coordinates2, coordinate]);
  }

  public showSingleElasticBand(anchor: Coordinate, coordinate: Coordinate) {
  }

  public updateSingleElasticBandPosition(coordinate: Coordinate) {
  }

  public hideSingleElasticBand() {
  }

}
