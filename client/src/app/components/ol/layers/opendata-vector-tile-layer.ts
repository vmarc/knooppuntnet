import { NetworkType } from '@api/custom';
import { Color } from 'ol/color';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import Style, { StyleFunction } from 'ol/style/Style';
import Text from 'ol/style/Text';
import { ZoomLevel } from '../domain';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class OpendataVectorTileLayer {
  private readonly largeMaxZoomResolution = /* zoomLevel 13 */ 19.109;
  private readonly smallStyle = this.buildSmallStyle();
  private readonly largeStyle = this.buildLargeStyle();

  build(networkType: NetworkType, layerName: string, dir: string): MapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: `/tiles-history/opendata/${dir}/{z}/{x}/{y}.mvt`,
    });

    const layer = new VectorTileLayer({
      zIndex: Layers.zIndexPoiLayer,
      source,
      renderBuffer: 40,
      declutter: false,
      className: 'poi',
      renderMode: 'vector',
    });

    layer.setStyle(this.styleFunction());

    return new MapLayer(
      layerName,
      `${layerName}-vector`,
      ZoomLevel.vectorTileMinZoom,
      ZoomLevel.vectorTileMaxOverZoom,
      layer,
      networkType,
      null
    );
  }

  private styleFunction(): StyleFunction {
    return (feature, resolution) => {
      const name = feature.get('name');
      const large = resolution < this.largeMaxZoomResolution;
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
    const lineDash = null; //[5, 5];
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
