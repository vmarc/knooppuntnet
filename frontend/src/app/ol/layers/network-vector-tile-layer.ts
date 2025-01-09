import { RouteType } from '@api/common';
import { Translations } from '@app/i18n';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { ZoomLevel } from '../domain';
import { OldLayers } from './old-layers';
import { OldMapLayer } from './old-map-layer';

export class NetworkVectorTileLayer {
  public static oldBuild(routeType: RouteType, styleFunction: StyleFunction): OldMapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: `/tiles/${routeType}/{z}/{x}/{y}.mvt`,
    });

    const layer = new VectorTileLayer({
      zIndex: OldLayers.zIndexNetworkLayer,
      source,
      renderMode: 'vector',
    });

    layer.setStyle(styleFunction);
    const name = Translations.get(`route-type.${routeType}`);
    return new OldMapLayer(
      routeType,
      name,
      ZoomLevel.vectorTileMinZoom,
      ZoomLevel.vectorTileMaxOverZoom,
      'vector',
      layer,
      routeType,
      null
    );
  }

  public static build(routeType: RouteType, styleFunction: StyleFunction): OldMapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: `/tiles/${routeType}/{z}/{x}/{y}.mvt`,
    });

    const layer = new VectorTileLayer({
      zIndex: OldLayers.zIndexNetworkLayer,
      className: `${routeType} - network`,
      declutter: false,
      source,
      renderMode: 'vector',
      style: styleFunction,
    });

    const name = Translations.get(`route-type.${routeType}`);
    return new OldMapLayer(
      routeType,
      name,
      ZoomLevel.vectorTileMinZoom,
      ZoomLevel.vectorTileMaxOverZoom,
      'vector',
      layer,
      routeType,
      null
    );
  }
}
