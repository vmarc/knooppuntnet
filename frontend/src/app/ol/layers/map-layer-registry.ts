import { MapLayer } from '.';
import { MapLayerState } from '../domain';

export class MapLayerRegistry {
  layers: MapLayer[] = [];
  layerStates: MapLayerState[] = [];

  register(
    urlLayerNames: string[],
    mapLayer: MapLayer,
    defaultVisible: boolean,
    enabled?: boolean
  ): void {
    let visible = defaultVisible;
    if (urlLayerNames.length > 0) {
      visible = urlLayerNames.includes(mapLayer.name);
    }
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
    urlLayerNames: string[],
    mapLayers: MapLayer[],
    defaultVisible: boolean,
    enabled?: boolean
  ): void {
    let visible = defaultVisible;
    if (urlLayerNames.length > 0) {
      visible = urlLayerNames.includes(mapLayers[0].name);
    }
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
