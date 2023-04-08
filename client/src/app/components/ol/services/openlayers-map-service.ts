import { InjectionToken } from '@angular/core';
import { inject } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { fromEvent } from 'rxjs';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import BaseLayer from 'ol/layer/Base';
import { UniqueId } from '@app/kpn/common/unique-id';
import { PageService } from '@app/components/shared/page.service';
import { Subscriptions } from '@app/util/Subscriptions';
import Map from 'ol/Map';
import { MapPosition } from '@app/components/ol/domain/map-position';

export const MAP_SERVICE_TOKEN = new InjectionToken<OpenlayersMapService>(
  'MAP_SERVICE_TOKEN'
);

export abstract class OpenlayersMapService {
  public mapId: string = UniqueId.get();
  private _map: Map;
  private _layerStates$ = new BehaviorSubject<MapLayerState[]>([]);
  protected mapLayers: MapLayer[] = [];
  public layerStates$ = this._layerStates$.asObservable();

  private _mapPosition$ = new BehaviorSubject<MapPosition | null>(null);
  public mapPosition$ = this._mapPosition$.asObservable();

  private readonly pageService = inject(PageService);
  private readonly subscriptions = new Subscriptions();
  private readonly updatePositionHandler = () => this.updateMapPosition();

  constructor() {
    this.subscriptions.add(
      this.pageService.sidebarOpen.subscribe(() => this.updateSize())
    );
    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() =>
        this.updateSize()
      )
    );
  }

  protected initMap(map: Map): void {
    this._map = map;
  }

  protected finalizeSetup(): void {
    const view = this.map.getView();
    view.on('change:resolution', () => this.updatePositionHandler);
    view.on('change:center', this.updatePositionHandler);
    this.updatePositionHandler();
    this.updateLayerVisibility();
  }

  get map(): Map {
    return this._map;
  }

  get layerStates(): MapLayerState[] {
    return this._layerStates$.value;
  }

  updateLayerStates(layerStates: MapLayerState[]) {
    this._layerStates$.next(layerStates);
  }

  get mapPosition(): MapPosition {
    return this._mapPosition$.value;
  }

  protected get layers(): BaseLayer[] {
    return this.mapLayers.map((mapLayer) => mapLayer.layer);
  }

  protected register(registry: MapLayerRegistry): void {
    this.mapLayers = registry.layers;
    this._layerStates$.next(registry.layerStates);
  }

  destroy(): void {
    this.subscriptions.unsubscribe();
    if (this.map) {
      this.map.getView().un('change:resolution', this.updatePositionHandler);
      this.map.getView().un('change:center', this.updatePositionHandler);
    }
    this._map.dispose();
    this._map.setTarget(null);
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

    this.updateLayerVisibility();
  }

  updateLayerVisibility(): void {
    const zoom = this._mapPosition$.value.zoom;
    const layerStates = this._layerStates$.value;
    this.mapLayers.forEach((mapLayer) => {
      const mapLayerState = layerStates.find(
        (layerState) => layerState.layerName === mapLayer.name
      );
      const zoomInRange = zoom >= mapLayer.minZoom && zoom <= mapLayer.maxZoom;
      const visible =
        zoomInRange &&
        mapLayerState &&
        mapLayerState.enabled &&
        mapLayerState.visible;
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

  private updateSize(): void {
    if (this._map) {
      setTimeout(() => {
        this._map.updateSize();
      }, 0);
    }
  }

  private updateMapPosition(): void {
    const center = this.map.getView().getCenter();
    if (center) {
      const zoom = this.map.getView().getZoom();
      const z = Math.round(zoom);
      const mapPosition = new MapPosition(z, center[0], center[1], 0);
      this._mapPosition$.next(mapPosition);
    }
  }
}
