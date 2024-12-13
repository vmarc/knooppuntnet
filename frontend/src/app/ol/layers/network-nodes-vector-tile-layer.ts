import { NetworkType } from '@api/common';
import { Translations } from '@app/i18n';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { ZoomLevel } from '../domain';
import { NetworkMapStyle } from '../style';
import { OldLayers } from './old-layers';
import { OldMapLayer } from './old-map-layer';

export class NetworkNodesVectorTileLayer {
  static build(
    networkType: NetworkType,
    networkNodeIds: number[],
    connectionNodeIds: number[],
    networkRouteIds: number[],
    connectionRouteIds: number[]
  ): OldMapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: `/tiles/${networkType}/{z}/{x}/{y}.mvt`,
    });

    const layer = new VectorTileLayer({
      zIndex: OldLayers.zIndexNetworkLayer,
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

    const name = Translations.get(`network-type.${networkType}`);
    return new OldMapLayer(
      `network-nodes-${networkType}-layer`,
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
