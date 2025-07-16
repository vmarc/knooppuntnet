import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import Stroke from 'ol/style/Stroke';
import Style, { StyleFunction } from 'ol/style/Style';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class MonitorLayer {
  private static readonly referenceStyle = new Style({
    zIndex: 1,
    stroke: new Stroke({
      color: 'yellow',
      width: 6,
    }),
  });

  private static readonly matchStyle = new Style({
    zIndex: 2,
    stroke: new Stroke({
      color: 'green',
      width: 3,
    }),
  });

  private static readonly deviationStyle = new Style({
    zIndex: 2,
    stroke: new Stroke({
      color: 'red',
      width: 3,
    }),
  });

  static build(): MapLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: ZoomLevel.newMinZoom,
      maxZoom: ZoomLevel.newMaxZoom,
      format: new MVT(),
      url: `/tiles/monitor/{z}/{x}/{y}.mvt`,
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
      layerType: 'monitor',
      minZoom: 2,
      maxZoom: 20,
      layer,
    };
  }

  private static styleFunction(): StyleFunction {
    return (feature, resolution) => {
      const layer = feature.get('layer');
      const group = feature.get('group');
      const route = feature.get('route');
      const relationId = feature.get('relationId');

      console.log(`${layer} ${group} ${route} ${relationId}`);
      //
      if (group === 'NL-LAW' && route === 'LAW 5' && relationId === '9174496') {
        if (layer === 'match') {
          return this.matchStyle;
        } else if (layer === 'reference') {
          return this.referenceStyle;
        } else if (layer === 'deviation') {
          return this.deviationStyle;
        }
      }
      // console.log(`unknown monitor tile layer: ${layer}`);
      return null;
    };
  }
}
