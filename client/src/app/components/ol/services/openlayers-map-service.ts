import { Injectable } from '@angular/core';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { BehaviorSubject } from 'rxjs';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { NewMapService } from '@app/components/ol/services/new-map.service';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import BaseLayer from 'ol/layer/Base';
import { UniqueId } from '@app/kpn/common/unique-id';

@Injectable()
export class OpenlayersMapService {
  public mapId: string = UniqueId.get();
  protected _map: OpenLayersMap;
  private _layerStates$ = new BehaviorSubject<MapLayerState[]>([]);
  private mapLayers: MapLayer[] = [];
  public layerStates$ = this._layerStates$.asObservable();

  constructor(protected newMapService: NewMapService) {}

  get map(): OpenLayersMap {
    return this._map;
  }

  protected get layers(): BaseLayer[] {
    return this.mapLayers.map((mapLayer) => mapLayer.layer);
  }

  protected register(registry: MapLayerRegistry): void {
    this.mapLayers = registry.layers;
    this._layerStates$.next(registry.layerStates);
    this.updateLayerVisibility(this._layerStates$.value);
  }

  destroy(): void {
    this._map.destroy();
  }

  layerStateChange(change: MapLayerState): void {
    const layerName = change.layerName;
    const visible = change.visible;

    const mapLayerStates = this._layerStates$.value.map((layerState) => {
      if (
        layerState.layerName == BackgroundLayer.id &&
        layerName == OsmLayer.id &&
        visible
      ) {
        return {
          ...layerState,
          visible: false,
        };
      }
      if (
        layerState.layerName == OsmLayer.id &&
        layerName == BackgroundLayer.id &&
        visible
      ) {
        return {
          ...layerState,
          visible: false,
        };
      }
      if (layerState.layerName === layerName) {
        return {
          ...layerState,
          visible,
        };
      }
      return layerState;
    });

    this._layerStates$.next(mapLayerStates);

    this.updateLayerVisibility(mapLayerStates);
  }

  updateLayerVisibility(layerStates: MapLayerState[]): void {
    this.mapLayers.forEach((mapLayer) => {
      const mapLayerState = layerStates.find(
        (layerState) => layerState.layerName === mapLayer.name
      );
      const visible = mapLayerState && mapLayerState.visible;
      if (
        visible &&
        mapLayer.id.includes('vector') &&
        mapLayer.layer.getVisible()
      ) {
        mapLayer.layer.changed();
      }
      mapLayer.layer.setVisible(visible);
    });
  }
}
