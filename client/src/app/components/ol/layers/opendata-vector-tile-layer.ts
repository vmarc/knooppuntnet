import { NetworkType } from '@api/custom/network-type';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { Color } from 'ol/color';
import { MVT } from 'ol/format';
import TileLayer from 'ol/layer/Tile';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
import VectorTile from 'ol/source/VectorTile';
import XYZ from 'ol/source/XYZ';
import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import Style, { StyleFunction } from 'ol/style/Style';
import Text from 'ol/style/Text';
import { ZoomLevel } from '../domain/zoom-level';
import { Layers } from './layers';

export class OpendataVectorTileLayer {
  private readonly largeMinZoomLevel = 13;
  private readonly smallStyle = this.buildSmallStyle();
  private readonly largeStyle = this.buildLargeStyle();
  bitmapTileLayer: TileLayer<XYZ>;
  vectorTileLayer: VectorTileLayer;

  build(networkType: NetworkType, layerName: string, dir: string): MapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: `/tiles-history/opendata/${dir}/{z}/{x}/{y}.mvt`,
    });

    this.vectorTileLayer = new VectorTileLayer({
      zIndex: Layers.zIndexPoiLayer,
      source,
      renderBuffer: 40,
      declutter: false,
      className: 'poi',
      renderMode: 'vector',
    });

    return new MapLayer(
      layerName,
      `${layerName}-vector`,
      ZoomLevel.vectorTileMinZoom,
      ZoomLevel.vectorTileMaxOverZoom,
      this.vectorTileLayer,
      null,
      null,
      this.applyMap()
    );
  }

  private applyMap() {
    return (map: Map) => {
      const style = this.styleFunction(map);
      this.vectorTileLayer.setStyle(style);
    };
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
