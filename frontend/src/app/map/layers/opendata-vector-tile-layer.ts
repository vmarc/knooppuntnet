import { RouteType } from '@api/common/route-type';
import { ZoomLevel } from '@app/ol/domain';
import { Color } from 'ol/color';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import Style, { StyleFunction } from 'ol/style/Style';
import Text from 'ol/style/Text';
import { LayerType } from './layer-type';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class OpendataVectorTileLayer {
  private static readonly largeMaxZoomResolution = /* zoomLevel 13 */ 19.109;
  private static readonly smallStyle = this.buildSmallStyle(false);
  private static readonly largeStyle = this.buildLargeStyle(false);
  private static readonly smallStyleVirtual = this.buildSmallStyle(true);
  private static readonly largeStyleVirtual = this.buildLargeStyle(true);

  static build(layerType: LayerType, routeType: RouteType, dir: string): MapLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: `/tiles/opendata/${dir}/{z}/{x}/{y}.mvt`,
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

    return {
      layerType,
      routeType,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxOverZoom,
      layer,
    };
  }

  private static styleFunction(): StyleFunction {
    return (feature, resolution) => {
      const name = feature.get('name');
      const virtual = feature.get('virtual') == 'true';
      const large = resolution < this.largeMaxZoomResolution;
      let style: Style | null = null;
      if (large) {
        if (virtual) {
          style = this.largeStyleVirtual;
        } else {
          style = this.largeStyle;
        }
        style.getText().setText(name);
      } else {
        if (virtual) {
          style = this.smallStyleVirtual;
        } else {
          style = this.smallStyle;
        }
      }
      return style;
    };
  }

  private static buildLargeStyle(virtual: boolean): Style {
    const red: Color = [255, 0, 0];
    const white: Color = [255, 255, 255];
    const lineDash = virtual ? [1, 10] : null;
    const circleDash = virtual ? [1, 6] : null;
    return new Style({
      stroke: new Stroke({
        color: red,
        lineDash,
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
          lineDash: circleDash,
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

  private static buildSmallStyle(virtual: boolean): Style {
    const red: Color = [255, 0, 0];
    const white: Color = [255, 255, 255];
    const lineDash = virtual ? [1, 5] : null;
    const lineWidth = virtual ? 2 : 3;
    return new Style({
      stroke: new Stroke({
        color: red,
        lineDash,
        width: lineWidth,
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
