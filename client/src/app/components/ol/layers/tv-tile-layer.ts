import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { ZoomLevel } from '../domain/zoom-level';
import { Layers } from './layers';
import { MapLayer } from "@app/components/ol/layers/map-layer";
import LayerGroup from "ol/layer/Group";
import Map from "ol/Map";
import Style, { StyleFunction } from "ol/style/Style";
import { Color } from "ol/color";
import Stroke from "ol/style/Stroke";
import Circle from "ol/style/Circle";
import Fill from "ol/style/Fill";
import Text from "ol/style/Text";
import { NetworkType } from "@api/custom/network-type";

export class TvTileLayer {

  private readonly largeMinZoomLevel = 13;
  private readonly smallStyle = this.buildSmallStyle();
  private readonly largeStyle = this.buildLargeStyle();
  bitmapTileLayer: MapLayer;
  vectorTileLayer: VectorTileLayer;

  build(networkType: NetworkType): MapLayer {

    /* TODO this.bitmapTileLayer.layer = ... */

    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.poiTileMinZoom,
      maxZoom: ZoomLevel.poiTileMaxZoom,
      format: new MVT(),
      url: '/tiles-history/toerisme-vlaanderen/hiking/{z}/{x}/{y}.mvt',
    });

    this.vectorTileLayer = new VectorTileLayer({
      zIndex: Layers.zIndexPoiLayer,
      source,
      renderBuffer: 40,
      declutter: false,
      className: 'poi',
      renderMode: 'vector',
    });

    const layer = new LayerGroup({
      layers: [ /* TODO this.bitmapTileLayer.layer,*/ this.vectorTileLayer],
    });

    const layerName = $localize`:@@map.layer.tv:Toerisme Vlaanderen`;
    layer.set('name', layerName);
    layer.setVisible(false);
    return new MapLayer(`tv-${networkType}-layer`, layer, this.applyMap());
  }

  private applyMap() {
    return (map: Map) => {
      const style = this.styleFunction(map);
      this.vectorTileLayer.setStyle(style);
      this.updateLayerVisibility(map.getView().getZoom());
      // TODO need to unsubscribe
      map
        .getView()
        .on('change:resolution', () => this.zoom(map.getView().getZoom()));
    };
  }

  private zoom(zoomLevel: number) {
    this.updateLayerVisibility(zoomLevel);
    return true;
  }

  private updateLayerVisibility(zoomLevel: number) {
    const zoom = Math.round(zoomLevel);
    if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
      /* TODO this.bitmapTileLayer.layer.setVisible(true); */
      this.vectorTileLayer.setVisible(false);
    } else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      /* TODO this.bitmapTileLayer.layer.setVisible(false); */
      this.vectorTileLayer.setVisible(true);
    }
  }

  private styleFunction(map: Map): StyleFunction {
    return (feature, resolution) => {
      const zoom = map.getView().getZoom();
      const name = feature.get('name');
      const large = zoom >= this.largeMinZoomLevel;
      let style = this.smallStyle;
      if (large) {
        style = this.largeStyle;
        style.getText().setText(name);
      }
      return style;
    };
  }

  private buildLargeStyle(): Style {
    const red: Color = [255, 0, 0];
    const white: Color = [255, 255, 255];
    return new Style({
      stroke: new Stroke({
        color: red,
        width: 6,
      }),
      image: new Circle({
        radius: 16,
        fill: new Fill({
          color: white,
        }),
        stroke: new Stroke({
          color: red,
          width: 4,
        }),
      }),
      text: new Text({
        text: '',
        textAlign: 'center',
        textBaseline: 'middle',
        font: '14px Arial, Verdana, Helvetica, sans-serif',
        stroke: new Stroke({
          color: white,
          width: 5,
        }),
      }),
    });
  }

  private buildSmallStyle(): Style {
    const red: Color = [255, 0, 0];
    const white: Color = [255, 255, 255];
    const lineDash = null;//[5, 5];
    return new Style({
      stroke: new Stroke({
        color: red,
        lineDash,
        width: 3,
      }),
      image: new Circle({
        radius: 3,
        fill: new Fill({
          color: white,
        }),
        stroke: new Stroke({
          color: red,
          width: 2,
        }),
      }),
    });
  }
}
