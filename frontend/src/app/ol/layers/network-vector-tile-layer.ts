import { NetworkType } from '@api/custom';
import { Translations } from '@app/i18n';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { ZoomLevel } from '../domain';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class NetworkVectorTileLayer {
  public static oldBuild(networkType: NetworkType, styleFunction: StyleFunction): MapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: '/tiles-history/' + networkType + '/{z}/{x}/{y}.mvt',
    });

    const layer = new VectorTileLayer({
      zIndex: Layers.zIndexNetworkLayer,
      source,
      renderMode: 'vector',
    });

    layer.setStyle(styleFunction);
    const name = Translations.get(`network-type.${networkType}`);
    return new MapLayer(
      networkType,
      name,
      ZoomLevel.vectorTileMinZoom,
      ZoomLevel.vectorTileMaxOverZoom,
      'vector',
      layer,
      networkType,
      null
    );
  }

  public static build(networkType: NetworkType, styleFunction: StyleFunction): MapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: '/tiles-history/' + networkType + '/{z}/{x}/{y}.mvt',
    });

    const layer = new VectorTileLayer({
      zIndex: Layers.zIndexNetworkLayer,
      className: `${networkType} - network`,
      declutter: false,
      source,
      renderMode: 'vector',
      style: styleFunction,
    });

    const name = Translations.get(`network-type.${networkType}`);
    return new MapLayer(
      networkType,
      name,
      ZoomLevel.vectorTileMinZoom,
      ZoomLevel.vectorTileMaxOverZoom,
      'vector',
      layer,
      networkType,
      null
    );
  }
}
