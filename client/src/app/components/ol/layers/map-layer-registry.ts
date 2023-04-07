import { MapLayer } from '@app/components/ol/layers/map-layer';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';

export class MapLayerRegistry {
  layers: MapLayer[] = [];
  layerStates: MapLayerState[] = [];

  register(mapLayer: MapLayer, visible: boolean): void {
    this.layers.push(mapLayer);
    this.layerStates.push({ layerName: mapLayer.name, visible });
  }

  registerAll(mapLayers: MapLayer[], visible: boolean): void {
    this.layers.push(...mapLayers);
    this.layerStates.push({ layerName: mapLayers[0].name, visible });
  }
}
