import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { InjectionToken } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { Bounds } from '@api/common';
import { PageService } from '@app/components/shared';
import { UniqueId } from '@app/kpn/common';
import { Subscriptions } from '@app/util';
import { Coordinate } from 'ol/coordinate';
import BaseLayer from 'ol/layer/Base';
import Map from 'ol/Map';
import { transformExtent } from 'ol/proj';
import { toLonLat } from 'ol/proj';
import { BehaviorSubject } from 'rxjs';
import { fromEvent } from 'rxjs';
import { distinct } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { MapGeocoder } from '../domain';
import { MapLayerState } from '../domain';
import { MapPosition } from '../domain';
import { MapLayer } from '../layers';
import { MapLayerRegistry } from '../layers';
import { OsmLayer } from '../layers';
import { BackgroundLayer } from '../layers/';

export const MAP_SERVICE_TOKEN = new InjectionToken<OpenlayersMapService>('MAP_SERVICE_TOKEN');

@Injectable()
export abstract class OpenlayersMapService {
  readonly mapId: string = UniqueId.get();
  private _map: Map;
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private shouldUpdateUrl = false;
  private _layerStates$ = new BehaviorSubject<MapLayerState[]>([]);
  protected mapLayers: MapLayer[] = [];
  public layerStates$ = this._layerStates$.asObservable();

  private _mapPosition$ = new BehaviorSubject<MapPosition | null>(null);
  public mapPosition$ = this._mapPosition$.pipe(distinct(), debounceTime(50));

  private readonly pageService = inject(PageService);
  private readonly subscriptions = new Subscriptions();
  private readonly updatePositionHandler = () => this.updateMapPosition();

  constructor() {
    effect(() => {
      this.pageService.sidebarOpen();
      this.updateSize();
    });
    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() => this.updateSize())
    );
  }

  protected initMap(map: Map): void {
    this._map = map;
    MapGeocoder.install(map);
  }

  protected finalizeSetup(updateUrl?: boolean): void {
    this.shouldUpdateUrl = updateUrl === true;
    const view = this.map.getView();
    view.on('change:resolution', this.updatePositionHandler);
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
      this.map.dispose();
      this.map.setTarget(null);
    }
  }

  layerStateChange(change: MapLayerState): void {
    const layerId = change.id;
    const visible = change.visible;

    const mapLayerStates = this._layerStates$.value.map((layerState) => {
      if (layerState.id == BackgroundLayer.id && layerId == OsmLayer.id && visible) {
        return {
          ...layerState,
          visible: false,
        };
      }
      if (layerState.id == OsmLayer.id && layerId == BackgroundLayer.id && visible) {
        return {
          ...layerState,
          visible: false,
        };
      }
      if (layerState.id === layerId) {
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
    this.mapLayers.forEach((mapLayer) => {
      const visible = this.layerVisible(mapLayer);
      mapLayer.layer.setVisible(visible);
    });

    // const visibleLayers = this.mapLayers
    //   .filter((mapLayer) => mapLayer.layer.getVisible() === true)
    //   .map((mapLayer) => mapLayer.id)
    //   .join(',');
    // console.log(['layerStates', this.layerStates]);
    // console.log(`updateLayerVisibility: ${visibleLayers}`);
  }

  protected layerVisible(mapLayer: MapLayer): boolean {
    const mapLayerState = this.layerStates.find((layerState) => layerState.id === mapLayer.id);
    if (mapLayerState) {
      const zoom = this.mapPosition.zoom;
      const zoomInRange = zoom >= mapLayer.minZoom && zoom <= mapLayer.maxZoom;
      const visible =
        zoomInRange &&
        (mapLayerState.enabled || mapLayerState.id === mapLayer.id) &&
        mapLayerState.visible;
      if (visible && mapLayer.mapTile == 'vector' && mapLayer.layer.getVisible()) {
        mapLayer.layer.changed();
      }
      return visible;
    }
  }

  mapBounds(): Bounds {
    if (this._map) {
      const extent = this._map.getView().calculateExtent(this._map.getSize());
      const transformedExtent = transformExtent(extent, 'EPSG:3857', 'EPSG:4326');
      return {
        minLat: transformedExtent[1],
        minLon: transformedExtent[0],
        maxLat: transformedExtent[3],
        maxLon: transformedExtent[2],
      };
    }
    return null;
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
        if (!this._mapPosition$.value.sameAs(mapPosition)) {
          this._mapPosition$.next(mapPosition);
        }
      } else {
        this._mapPosition$.next(mapPosition);
      }
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
