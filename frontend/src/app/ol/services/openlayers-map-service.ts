import { signal } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { InjectionToken } from '@angular/core';
import { inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { Coordinate } from '@api/custom/coordinate';
import { PageService } from '@app/shared/components/page.service';
import { UniqueId } from '@app/shared/kpn/common/unique-id';
import { Subscriptions } from '@app/util/subscriptions';
import BaseLayer from 'ol/layer/Base';
import Map from 'ol/Map';
import { toLonLat } from 'ol/proj';
import { BehaviorSubject } from 'rxjs';
import { fromEvent } from 'rxjs';
import { distinct } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { MapGeocoder } from '../domain/map-geocoder';
import { MapLayerState } from '../domain/map-layer-state';
import { MapPosition } from '../domain/map-position';
import { OldOldMapLayer } from '../layers/old-old-map-layer';
import { OldOldMapLayerRegistry } from '../layers/old-old-map-layer-registry';

export const MAP_SERVICE_TOKEN = new InjectionToken<OpenlayersMapService>('MAP_SERVICE_TOKEN');

@Injectable()
export abstract class OpenlayersMapService {
  readonly mapId: string = UniqueId.get();
  private _map: Map;
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private shouldUpdateUrl = false;

  private readonly _layerStates = signal<MapLayerState[]>([]);

  protected mapLayers: OldOldMapLayer[] = [];

  readonly layerStates = this._layerStates.asReadonly();

  private _mapPosition$ = new BehaviorSubject<MapPosition | null>(null);
  public mapPosition = toSignal(this._mapPosition$.pipe(distinct(), debounceTime(50)));

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
    effect(() => {
      const mapPosition = this.mapPosition();
      if (mapPosition && this.shouldUpdateUrl) {
        this.updateUrl(mapPosition);
      }
    });
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
  }

  get map(): Map {
    return this._map;
  }

  protected get layers(): BaseLayer[] {
    return this.mapLayers.map((mapLayer) => mapLayer.layer);
  }

  protected register(registry: OldOldMapLayerRegistry): void {
    this.mapLayers = registry.layers;
    this._layerStates.set(registry.layerStates);
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

  updateLayerVisibility(): void {
    this.mapLayers.forEach((mapLayer) => {
      const visible = this.layerVisible(mapLayer);
      mapLayer.layer.setVisible(visible);
    });

    const visibleLayers = this.mapLayers
      .filter((mapLayer) => mapLayer.layer.getVisible() === true)
      .map((mapLayer) => mapLayer.id)
      .join(',');
    if (this.shouldUpdateUrl) {
      const params: Params = {
        layers: visibleLayers,
      };
      this.setQueryParams(params);
    }
  }

  protected layerVisible(mapLayer: OldOldMapLayer): boolean {
    const mapLayerState = this.layerStates().find((layerState) => layerState.id === mapLayer.id);
    if (mapLayerState) {
      const zoom = this._mapPosition$.value.zoom;
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
    return false;
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
