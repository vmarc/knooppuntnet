import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Bounds } from '@api/common/bounds';
import { NetworkType } from '@api/custom/network-type';
import { MainMapStyle } from '@app/components/ol/style/main-map-style';
import { MainMapStyleParameters } from '@app/components/ol/style/main-map-style-parameters';
import { NetworkTypes } from '@app/kpn/common/network-types';
import { Subscriptions } from '@app/util/Subscriptions';
import { List } from 'immutable';
import Map from 'ol/Map';
import View from 'ol/View';
import { Observable } from 'rxjs';
import { fromEvent } from 'rxjs';
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
      <kpn-old-layer-switcher [mapLayers]="layers"/>
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
  ) {}

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

    this.mapClickService.installOn(this.map);

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
    throw new Error('TODO implement options$');
    const parameters$: Observable<MainMapStyleParameters> = null;
    const mainMapStyle = new MainMapStyle(parameters$);

    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(
      this.mapLayerService.backgroundLayer(this.mapId)
    );
    NetworkTypes.all.forEach((networkType) => {
      mapLayers = mapLayers.push(
        this.mapLayerService.mainMapVectorLayer(
          networkType,
          mainMapStyle.styleFunction()
        )
      );
      mapLayers = mapLayers.push(
        this.mapLayerService.mainMapBitmapLayer(networkType)
      );
    });
    mapLayers = mapLayers.push(
      this.mapLayerService.locationBoundaryLayer(this.geoJson)
    );
    return new MapLayers(mapLayers);
  }
}
