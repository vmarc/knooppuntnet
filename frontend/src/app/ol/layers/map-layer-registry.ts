import { MapLayer } from '.';
import { MapLayerState } from '../domain';

export class MapLayerRegistry {
  layers: MapLayer[] = [];
  layerStates: MapLayerState[] = [];

  register(
    urlLayerIds: string[],
    mapLayer: MapLayer,
    defaultVisible: boolean,
    enabled?: boolean
  ): void {
    let visible = defaultVisible;
    if (urlLayerIds.length > 0) {
      visible = urlLayerIds.includes(mapLayer.id);
    }
    let layerEnabled = enabled;
    if (enabled === undefined || enabled === null) {
      layerEnabled = true;
    }
    this.layers.push(mapLayer);
    this.layerStates.push({
      id: mapLayer.id,
      name: mapLayer.name,
      visible,
      enabled: layerEnabled,
    });
  }

  registerAll(
    urlLayerIds: string[],
    mapLayers: MapLayer[],
    defaultVisible: boolean,
    enabled?: boolean
  ): void {
    let visible = defaultVisible;
    if (urlLayerIds.length > 0) {
      visible = urlLayerIds.includes(mapLayers[0].id);
    }
    let layerEnabled = enabled;
    if (enabled === undefined || enabled === null) {
      layerEnabled = true;
    }
    this.layers.push(...mapLayers);
    this.layerStates.push({
      id: mapLayers[0].id,
      name: mapLayers[0].name,
      visible,
      enabled: layerEnabled,
    });
  }
}
