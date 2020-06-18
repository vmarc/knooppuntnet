import Feature from "ol/Feature";
import GeometryType from "ol/geom/GeometryType";
import VectorLayer from "ol/layer/Vector";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import Circle from "ol/style/Circle";
import Fill from "ol/style/Fill";
import Stroke from "ol/style/Stroke";
import {StyleFunction} from "ol/style/Style";
import Style from "ol/style/Style";
import {Layers} from "../../../components/ol/layers/layers";
import {PlannerHighlightLayer} from "./planner-highlight-layer";

export class PlannerHighlightLayerImpl implements PlannerHighlightLayer {

  private readonly yellow = "rgba(255, 255, 0, 0.8)";
  private readonly largeMinZoomLevel = 13;
  private readonly smallNodeSelectedStyle = this.nodeSelectedStyle(12);
  private readonly largeNodeSelectedStyle = this.nodeSelectedStyle(22);
  private readonly largeRouteSelectedStyle = new Style({
    stroke: new Stroke({
      color: this.yellow,
      width: 18
    })
  });


  private map: Map;

  private highlightedFeature: Feature = null;

  private source = new VectorSource({
    features: []
  });

  private layer = new VectorLayer({
    zIndex: Layers.zIndexHighlightLayer,
    source: this.source
  });

  constructor() {
  }

  addToMap(map: Map) {
    this.map = map;
    this.layer.setStyle(this.styleFunction());
    this.layer.setVisible(true);
    map.addLayer(this.layer);
  }

  public styleFunction(): StyleFunction {
    return (feature, resolution) => {
      const zoom = this.map.getView().getZoom();
      const large = zoom >= this.largeMinZoomLevel;
      if (feature.getGeometry().getType() === GeometryType.POINT) {
        if (large) {
          return this.largeNodeSelectedStyle;
        }
        return this.smallNodeSelectedStyle;
      }
      if (large) {
        return this.largeRouteSelectedStyle;
      }
      return this.largeRouteSelectedStyle;
    };
  }

  highlightFeature(feature: Feature): void {
    if (feature !== this.highlightedFeature && this.highlightedFeature !== null) {
      this.source.removeFeature(this.highlightedFeature);
    }
    this.highlightedFeature = feature;
    this.source.addFeature(feature);
    this.layer.changed();
  }

  reset(): void {
    if (this.highlightedFeature !== null) {
      this.source.removeFeature(this.highlightedFeature);
      this.highlightedFeature = null;
    }
    this.layer.changed();
  }

  private nodeSelectedStyle(radius: number): Style {
    return new Style({
      image: new Circle({
        radius: radius,
        fill: new Fill({
          color: this.yellow
        })
      })
    });
  }
}
