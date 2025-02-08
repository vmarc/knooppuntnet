import { OldMapLayer } from '@app/ol/layers/old-map-layer';
import { MapLayerState } from '../domain/map-layer-state';

export class OldMapLayerRegistry {
  layers: OldMapLayer[] = [];
  layerStates: MapLayerState[] = [];

  register(
    urlLayerIds: string[],
    mapLayer: OldMapLayer,
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
    mapLayers: OldMapLayer[],
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
