import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Bounds } from '@api/common/bounds';
import { NetworkType } from '@api/custom/network-type';
import { List } from 'immutable';
import Map from 'ol/Map';
import View from 'ol/View';
import { Subscriptions } from '../../../util/Subscriptions';
import { PageService } from '../../shared/page.service';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { MapClickService } from '../services/map-click.service';
import { MapLayerService } from '../services/map-layer.service';
import { MapService } from '../services/map.service';

@Component({
  selector: 'kpn-location-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="location-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `,
})
export class LocationMapComponent implements AfterViewInit, OnDestroy {
  @Input() networkType: NetworkType;
  @Input() bounds: Bounds;
  @Input() geoJson: string;

  layers: MapLayers;
  private map: Map;
  private readonly mapId = 'location-map';
  private readonly subscriptions = new Subscriptions();

  constructor(
    private activatedRoute: ActivatedRoute,
    private pageService: PageService,
    private mapService: MapService,
    private mapClickService: MapClickService,
    private mapLayerService: MapLayerService
  ) {
    this.pageService.showFooter = false;
  }

  ngAfterViewInit(): void {
    this.mapService.nextNetworkType(this.networkType);
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
        maxZoom: ZoomLevel.vectorTileMaxOverZoom,
      }),
    });

    this.map.getView().fit(Util.toExtent(this.bounds, 0.05));
    this.layers.applyMap(this.map);

    this.mapClickService.installOn(this.map);

    this.subscriptions.add(
      this.pageService.sidebarOpen.subscribe(() => {
        if (this.map) {
          setTimeout(() => {
            this.map.updateSize();
            this.layers.updateSize();
          }, 0);
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
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
    mapLayers = mapLayers.push(this.mapLayerService.mainMapLayer());
    mapLayers = mapLayers.push(
      this.mapLayerService.locationBoundaryLayer(this.geoJson)
    );
    return new MapLayers(mapLayers);
  }
}
