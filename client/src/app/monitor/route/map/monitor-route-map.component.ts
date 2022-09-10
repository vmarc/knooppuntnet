import { Input } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { MapLayers } from '@app/components/ol/layers/map-layers';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { PageService } from '@app/components/shared/page.service';
import { Util } from '@app/components/shared/util';
import { AppState } from '@app/core/core.state';
import { I18nService } from '@app/i18n/i18n.service';
import { Subscriptions } from '@app/util/Subscriptions';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import Map from 'ol/Map';
import View from 'ol/View';
import { fromEvent } from 'rxjs';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'kpn-monitor-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="monitor-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="mapLayers"></kpn-layer-switcher>
    </div>
  `,
})
export class MonitorRouteMapComponent implements AfterViewInit, OnDestroy {
  @Input() page: MonitorRouteMapPage;

  mapLayers: MapLayers;
  map: Map;

  private readonly subscriptions = new Subscriptions();

  constructor(
    private i18nService: I18nService,
    private pageService: PageService,
    private mapService: MonitorRouteMapService,
    private store: Store<AppState>
  ) {}

  ngAfterViewInit(): void {
    const layers: MapLayer[] = [];
    const osmLayer = new OsmLayer(this.i18nService).build();
    osmLayer.layer.setVisible(false);
    layers.push(osmLayer);

    const backgroundLayer = new BackgroundLayer(this.i18nService).build(
      'monitor-map'
    );
    backgroundLayer.layer.setVisible(true);
    layers.push(backgroundLayer);

    this.mapLayers = new MapLayers(List(layers));

    this.map = new Map({
      target: 'monitor-map',
      layers: this.mapLayers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: 0,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom,
      }),
    });

    this.mapService.setMap(this.map);

    this.mapService.layers().forEach((layer) => {
      this.map.addLayer(layer);
    });

    this.map.getView().fit(Util.toExtent(this.page.bounds, 0.05));

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
        this.mapLayers.updateSize();
      }, 0);
    }
  }

  ngOnDestroy(): void {
    this.mapService.setMap(null);
    this.subscriptions.unsubscribe();
    if (this.map) {
      this.map.dispose();
      this.map.setTarget(null);
    }
  }
}
