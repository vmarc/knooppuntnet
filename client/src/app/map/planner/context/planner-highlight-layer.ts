import {FeatureLike} from 'ol/Feature';
import Feature from 'ol/Feature';
import GeometryType from 'ol/geom/GeometryType';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import {StyleFunction} from 'ol/style/Style';
import Style from 'ol/style/Style';
import {Layers} from '../../../components/ol/layers/layers';

export class PlannerHighlightLayer {

  private readonly largeMinZoomLevel = 13;
  private readonly yellow = 'rgba(255, 255, 0, 0.8)';
  private readonly blue = 'rgba(0, 0, 255, 0.5)';

  private readonly routeStyle = PlannerHighlightLayer.buildRouteStyle(this.yellow);
  private readonly smallNodeStyle = PlannerHighlightLayer.buildNodeStyle(12, this.yellow);
  private readonly largeNodeStyle = PlannerHighlightLayer.buildNodeStyle(22, this.yellow);
  private readonly mouseDownStyle = PlannerHighlightLayer.buildNodeStyle(30, this.blue);

  private map: Map;

  private source = new VectorSource({
    features: []
  });

  private layer = new VectorLayer({
    zIndex: Layers.zIndexHighlightLayer,
    source: this.source
  });

  private static buildNodeStyle(radius: number, color: string): Style {
    return new Style({
      image: new Circle({
        radius,
        fill: new Fill({
          color
        })
      })
    });
  }

  private static buildRouteStyle(color: string): Style {
    return new Style({
      stroke: new Stroke({
        color,
        width: 18
      })
    });
  }

  addToMap(map: Map) {
    this.map = map;
    this.layer.setStyle(this.styleFunction());
    this.layer.setVisible(true);
    map.addLayer(this.layer);
  }

  styleFunction(): StyleFunction {
    return (feature: FeatureLike) => {
      if (feature.getGeometry().getType() === GeometryType.POINT) {
        const zoom = this.map.getView().getZoom();
        if ('true' === feature.get('mouse-down')) {
          return this.mouseDownStyle;
        }
        return zoom >= this.largeMinZoomLevel ? this.largeNodeStyle : this.smallNodeStyle;
      }
      return this.routeStyle;
    };
  }

  highlightFeature(feature: Feature): void {
    this.source.clear(true);
    this.source.addFeature(feature);
    this.layer.changed();
  }

  reset(): void {
    this.source.clear(true);
    this.layer.changed();
  }

}
