import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Input } from '@angular/core';
import { Output } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { SubsetMapNetwork } from '@api/common/subset/subset-map-network';
import { Subscriptions } from '@app/util/Subscriptions';
import { List } from 'immutable';
import { MapBrowserEvent } from 'ol';
import { FeatureLike } from 'ol/Feature';
import Interaction from 'ol/interaction/Interaction';
import Map from 'ol/Map';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import View from 'ol/View';
import { fromEvent } from 'rxjs';
import { PageService } from '../../shared/page.service';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayers } from '../layers/map-layers';
import { NetworkMarkerLayer } from '../layers/network-marker-layer';
import { MapLayerService } from '../services/map-layer.service';

@Component({
  selector: 'kpn-subset-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="subset-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `,
})
export class SubsetMapComponent implements AfterViewInit, OnDestroy {
  @Input() bounds: Bounds;
  @Input() networks: SubsetMapNetwork[];
  @Output() networkClicked = new EventEmitter<number>();

  layers: MapLayers;
  private map: Map;

  private readonly mapId = 'subset-map';
  private readonly subscriptions = new Subscriptions();

  constructor(
    private mapLayerService: MapLayerService,
    private pageService: PageService
  ) {}

  ngAfterViewInit(): void {
    this.layers = this.buildLayers();
    setTimeout(
      () => this.mapLayerService.restoreMapLayerStates(this.layers),
      0
    );
    this.map = new Map({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
      }),
    });

    this.layers.applyMap(this.map);
    const view = this.map.getView();
    view.fit(Util.toExtent(this.bounds, 0.1));

    this.map.addInteraction(this.buildInteraction());

    this.subscriptions.add(
      this.pageService.sidebarOpen.subscribe(() => this.updateSize())
    );
    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() =>
        this.updateSize()
      )
    );
  }

  private updateSize(): void {
    if (this.map) {
      setTimeout(() => {
        this.map.updateSize();
        this.layers.updateSize();
      }, 0);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    if (this.map) {
      this.map.setTarget(null);
    }
  }

  private buildLayers(): MapLayers {
    return new MapLayers(
      List([
        this.mapLayerService.backgroundLayer(this.mapId),
        this.mapLayerService.networkMarkerLayer(this.networks),
      ])
    );
  }

  private buildInteraction(): Interaction {
    return new Interaction({
      handleEvent: (event: MapBrowserEvent<MouseEvent>) => {
        if (MapBrowserEventType.SINGLECLICK === event.type) {
          return this.handleSingleClickEvent(event);
        }
        if (MapBrowserEventType.POINTERMOVE === event.type) {
          return this.handleMoveEvent(event);
        }
        return true; // propagate event
      },
    });
  }

  private handleSingleClickEvent(evt: MapBrowserEvent<MouseEvent>): boolean {
    const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {
      hitTolerance: 10,
    });
    if (features) {
      const index = features.findIndex(
        (feature) =>
          NetworkMarkerLayer.networkMarker ===
          feature.get(NetworkMarkerLayer.layer)
      );
      if (index >= 0) {
        const networkId = features[index].get(NetworkMarkerLayer.networkId);
        this.networkClicked.emit(+networkId);
        return false; // do not propagate event
      }
    }
    return true; // propagate event
  }

  private handleMoveEvent(evt: MapBrowserEvent<MouseEvent>): boolean {
    const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {
      hitTolerance: 10,
    });
    if (features) {
      const index = features.findIndex(
        (feature) =>
          NetworkMarkerLayer.networkMarker ===
          feature.get(NetworkMarkerLayer.layer)
      );
      evt.map.getTargetElement().style.cursor =
        index >= 0 ? 'pointer' : 'default';
    }
    return true; // propagate event
  }
}
