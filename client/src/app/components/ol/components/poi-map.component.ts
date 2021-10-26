import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { NetworkType } from '@api/custom/network-type';
import { Subscriptions } from '@app/util/Subscriptions';
import { List } from 'immutable';
import Map from 'ol/Map';
import View from 'ol/View';
import { fromEvent } from 'rxjs';
import { PageService } from '../../shared/page.service';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { MapLayerService } from '../services/map-layer.service';
import { MapMode } from '../services/map-mode';

@Component({
  selector: 'kpn-poi-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="poi-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `,
})
export class PoiMapComponent implements AfterViewInit, OnDestroy {
  @Input() geoJson: string;

  layers: MapLayers;
  private map: Map;
  private readonly mapId = 'poi-map';
  private readonly subscriptions = new Subscriptions();

  constructor(
    private mapLayerService: MapLayerService,
    private pageService: PageService
  ) {}

  ngAfterViewInit(): void {
    this.layers = this.buildLayers();
    const center = Util.toCoordinate('49.153', '2.4609');
    this.map = new Map({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        center,
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 8,
      }),
    });

    this.layers.applyMap(this.map);

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
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(
      this.mapLayerService.backgroundLayer(this.mapId)
    );
    mapLayers = mapLayers.push(
      this.mapLayerService.networkBitmapTileLayer(
        NetworkType.cycling,
        MapMode.analysis
      )
    );
    mapLayers = mapLayers.concat(
      this.mapLayerService.poiAreasLayer(this.geoJson)
    );
    mapLayers = mapLayers.push(this.mapLayerService.tile256NameLayer());
    return new MapLayers(mapLayers);
  }
}
