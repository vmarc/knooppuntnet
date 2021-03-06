import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
import VectorTile from 'ol/source/VectorTile';
import { NetworkType } from '@api/custom/network-type';
import { ZoomLevel } from '../domain/zoom-level';
import { NodeMapStyle } from '../style/node-map-style';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class NetworkVectorTileLayer {
  public static oldBuild(networkType: NetworkType): VectorTileLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom - 1,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: '/tiles/' + networkType + '/{z}/{x}/{y}.mvt',
    });

    return new VectorTileLayer({
      zIndex: Layers.zIndexNetworkLayer,
      source,
      renderMode: 'vector',
    });
  }

  public static build(networkType: NetworkType): MapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom - 1,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: '/tiles/' + networkType + '/{z}/{x}/{y}.mvt',
    });

    const layer = new VectorTileLayer({
      zIndex: Layers.zIndexNetworkLayer,
      className: `${networkType}-network`,
      declutter: true,
      source,
      renderMode: 'vector',
    });

    const applyMap = (map: Map) => {
      const nodeMapStyle = new NodeMapStyle(map).styleFunction();
      layer.setStyle(nodeMapStyle);
    };

    return new MapLayer(`${networkType}`, layer, applyMap);
  }
}
