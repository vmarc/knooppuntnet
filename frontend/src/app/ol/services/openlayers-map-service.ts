import { Injectable } from '@angular/core';
import { InjectionToken } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { PageService } from '@app/components/shared';
import { UniqueId } from '@app/kpn/common';
import { Subscriptions } from '@app/util';
import { Coordinate } from 'ol/coordinate';
import BaseLayer from 'ol/layer/Base';
import Map from 'ol/Map';
import { toLonLat } from 'ol/proj';
import { BehaviorSubject } from 'rxjs';
import { fromEvent } from 'rxjs';
import { distinct } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { MapLayerState } from '../domain';
import { MapPosition } from '../domain';
import { MapLayer } from '../layers';
import { MapLayerRegistry } from '../layers';
import { OsmLayer } from '../layers';
import { BackgroundLayer } from '../layers/';

export const MAP_SERVICE_TOKEN = new InjectionToken<OpenlayersMapService>('MAP_SERVICE_TOKEN');

@Injectable()
export abstract class OpenlayersMapService {
  public mapId: string = UniqueId.get();
  private _map: Map;
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private shouldUpdateUrl = false;
  private _layerStates$ = new BehaviorSubject<MapLayerState[]>([]);
  protected mapLayers: MapLayer[] = [];
  public layerStates$ = this._layerStates$.asObservable();

  // TODO private readonly _mapPosition = signal<MapPosition | null>(null);
  // TODO readonly mapPosition = this._mapPosition.asReadonly();
  private _mapPosition$ = new BehaviorSubject<MapPosition | null>(null);
  public mapPosition$ = this._mapPosition$.pipe(distinct(), debounceTime(50));

  private readonly pageService = inject(PageService);
  private readonly subscriptions = new Subscriptions();
  private readonly updatePositionHandler = () => this.updateMapPosition();

  constructor() {
    this.subscriptions.add(this.pageService.sidebarOpen.subscribe(() => this.updateSize()));
    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() => this.updateSize())
    );
  }

  protected initMap(map: Map): void {
    this._map = map;
  }

  protected finalizeSetup(updateUrl?: boolean): void {
    this.shouldUpdateUrl = updateUrl === true;
    const view = this.map.getView();
    view.on('change:resolution', () => this.updatePositionHandler);
    view.on('change:center', this.updatePositionHandler);
    this.updatePositionHandler();
    this.updateLayerVisibility();
    if (this.shouldUpdateUrl) {
      this.subscriptions.add(
        this.mapPosition$.subscribe((mapPosition) => {
          this.updateUrl(mapPosition);
        })
      );
    }
  }

  get map(): Map {
    return this._map;
  }

  protected get layerStates(): MapLayerState[] {
    return this._layerStates$.value;
  }

  protected updateLayerStates(layerStates: MapLayerState[]) {
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
      if (layerState.layerName == BackgroundLayer.id && layerName == OsmLayer.id && visible) {
        return {
          ...layerState,
          visible: false,
        };
      }
      if (layerState.layerName == OsmLayer.id && layerName == BackgroundLayer.id && visible) {
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
      if (mapLayerState) {
        const zoomInRange = zoom >= mapLayer.minZoom && zoom <= mapLayer.maxZoom;
        const visible =
          zoomInRange &&
          (mapLayerState.enabled || mapLayerState.layerName === mapLayer.name) &&
          mapLayerState.visible;
        if (visible && mapLayer.id.includes('vector') && mapLayer.layer.getVisible()) {
          mapLayer.layer.changed();
        }
        mapLayer.layer.setVisible(visible);
      }
    });

    // const visibleLayers = this.mapLayers
    //   .filter((mapLayer) => mapLayer.layer.getVisible() === true)
    //   .map((mapLayer) => mapLayer.id)
    //   .join(',');
    // console.log(`updateLayerVisibility: ${visibleLayers}`);
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
      let oldZoom = -1;
      if (this._mapPosition$.value) {
        oldZoom = this._mapPosition$.value.zoom;
      }
      this._mapPosition$.next(mapPosition);
      if (oldZoom > 0 && oldZoom != z) {
        this.updateLayerVisibility();
      }
    }
  }

  private updateUrl(mapPosition: MapPosition): void {
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    const zoom = mapPosition.zoom;

    const c = toLonLat(center);
    const lng = c[0].toFixed(8);
    const lat = c[1].toFixed(8);
    const z = Math.round(zoom);

    const position = `${lat},${lng},${z}`;
    this.setQueryParams({ position });
  }

  protected setQueryParams(queryParams: Params): void {
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams,
      replaceUrl: true, // do not push a new entry to the browser history
      queryParamsHandling: 'merge', // preserve other query params if there are any
    });
  }
}
