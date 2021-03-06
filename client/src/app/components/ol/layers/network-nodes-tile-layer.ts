import { NetworkType } from '@api/custom/network-type';
import { MVT } from 'ol/format';
import LayerGroup from 'ol/layer/Group';
import TileLayer from 'ol/layer/Tile';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
import VectorTile from 'ol/source/VectorTile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain/zoom-level';
import { NetworkNodesMapStyle } from '../style/network-nodes-map-style';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class NetworkNodesTileLayer {
  static build(
    networkType: NetworkType,
    nodeIds: number[],
    routeIds: number[]
  ): MapLayer {
    const bitmapLayer = this.buildBitmapLayer(networkType);
    const vectorLayer = this.buildVectorLayer(networkType);

    const layer = new LayerGroup({
      layers: [bitmapLayer, vectorLayer],
      minZoom: ZoomLevel.bitmapTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxOverZoom,
    });

    const applyMap = (map: Map) => {
      const updateLayerVisibility = () => {
        const zoom = Math.round(map.getView().getZoom());
        if (zoom < ZoomLevel.vectorTileMinZoom) {
          bitmapLayer.setVisible(true);
          vectorLayer.setVisible(false);
        } else {
          bitmapLayer.setVisible(false);
          vectorLayer.setVisible(true);
        }
      };

      const nodeMapStyle = new NetworkNodesMapStyle(
        map,
        nodeIds,
        routeIds
      ).styleFunction();
      vectorLayer.setStyle(nodeMapStyle);
      updateLayerVisibility();
      map.getView().on('change:resolution', () => updateLayerVisibility());
      return true;
    };

    return new MapLayer(`network-nodes-${networkType}-layer`, layer, applyMap);
  }

  private static buildBitmapLayer(networkType: NetworkType): TileLayer {
    return new TileLayer({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: `/tiles/${networkType}/analysis/{z}/{x}/{y}.png`,
      }),
    });
  }

  private static buildVectorLayer(networkType: NetworkType): VectorTileLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom - 1,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: '/tiles/' + networkType + '/{z}/{x}/{y}.mvt',
    });

    return new VectorTileLayer({
      zIndex: Layers.zIndexNetworkLayer,
      className: 'network-layer',
      source,
      renderMode: 'vector',
    });
  }
}
