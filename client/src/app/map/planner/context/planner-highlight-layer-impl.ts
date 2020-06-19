import {FeatureLike} from "ol/Feature";
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

  private readonly largeMinZoomLevel = 13;
  private readonly yellow = "rgba(255, 255, 0, 0.8)";
  private readonly routeStyle = this.buildRouteStyle();
  private readonly smallNodeStyle = this.buildNodeStyle(12);
  private readonly largeNodeStyle = this.buildNodeStyle(22);

  private map: Map;

  private source = new VectorSource({
    features: []
  });

  private layer = new VectorLayer({
    zIndex: Layers.zIndexHighlightLayer,
    source: this.source
  });

  addToMap(map: Map) {
    this.map = map;
    this.layer.setStyle(this.styleFunction());
    this.layer.setVisible(true);
    map.addLayer(this.layer);
  }

  public styleFunction(): StyleFunction {
    return (feature: FeatureLike) => {
      if (feature.getGeometry().getType() === GeometryType.POINT) {
        const zoom = this.map.getView().getZoom();
        return zoom >= this.largeMinZoomLevel ? this.largeNodeStyle : this.smallNodeStyle;
      }
      return this.routeStyle;
    };
  }

  highlightFeature(feature: Feature): void {
    this.source.clear();
    this.source.addFeature(feature);
    this.layer.changed();
  }

  reset(): void {
    this.source.clear();
    this.layer.changed();
  }

  private buildNodeStyle(radius: number): Style {
    return new Style({
      image: new Circle({
        radius: radius,
        fill: new Fill({
          color: this.yellow
        })
      })
    });
  }

  private buildRouteStyle(): Style {
    return new Style({
      stroke: new Stroke({
        color: this.yellow,
        width: 18
      })
    });
  }
}
