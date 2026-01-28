import { RouteType } from '@api/common/route-type';
import { Translations } from '@app/shared/i18n/translations';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { ZoomLevel } from '../domain/zoom-level';
import { NetworkMapStyle } from '../style/network-map-style';
import { OldOldLayers } from './old-old-layers';
import { OldOldMapLayer } from './old-old-map-layer';

export class NetworkNodesVectorTileLayer {
  static build(
    routeType: RouteType,
    networkNodeIds: number[],
    connectionNodeIds: number[],
    networkRouteIds: number[],
    connectionRouteIds: number[]
  ): OldOldMapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: `/tiles/${routeType}/{z}/{x}/{y}.mvt`,
    });

    const layer = new VectorTileLayer({
      zIndex: OldOldLayers.zIndexNetworkLayer,
      className: 'network-layer',
      source,
      renderMode: 'vector',
    });

    const nodeMapStyle = new NetworkMapStyle(
      networkNodeIds,
      connectionNodeIds,
      networkRouteIds,
      connectionRouteIds
    ).styleFunction();
    layer.setStyle(nodeMapStyle);

    const name = Translations.get(`route-type.${routeType}`);
    return new OldOldMapLayer(
      `network-nodes-${routeType}-layer`,
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
