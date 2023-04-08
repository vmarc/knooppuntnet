import { MapLayer } from '@app/components/ol/layers/map-layer';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';

export class MapLayerRegistry {
  layers: MapLayer[] = [];
  layerStates: MapLayerState[] = [];

  register(mapLayer: MapLayer, visible: boolean, enabled?: boolean): void {
    let layerEnabled = enabled;
    if (enabled === undefined || enabled === null) {
      layerEnabled = true;
    }
    this.layers.push(mapLayer);
    this.layerStates.push({
      layerName: mapLayer.name,
      visible,
      enabled: layerEnabled,
    });
  }

  registerAll(
    mapLayers: MapLayer[],
    visible: boolean,
    enabled?: boolean
  ): void {
    let layerEnabled = enabled;
    if (enabled === undefined || enabled === null) {
      layerEnabled = true;
    }
    this.layers.push(...mapLayers);
    this.layerStates.push({
      layerName: mapLayers[0].name,
      visible,
      enabled: layerEnabled,
    });
  }
}
