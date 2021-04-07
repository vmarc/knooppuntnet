import { OnInit } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import Map from 'ol/Map';
import View from 'ol/View';
import { filter } from 'rxjs/operators';
import { ZoomLevel } from '../../../../components/ol/domain/zoom-level';
import { BackgroundLayer } from '../../../../components/ol/layers/background-layer';
import { MapControls } from '../../../../components/ol/layers/map-controls';
import { MapLayer } from '../../../../components/ol/layers/map-layer';
import { MapLayers } from '../../../../components/ol/layers/map-layers';
import { OsmLayer } from '../../../../components/ol/layers/osm-layer';
import { PageService } from '../../../../components/shared/page.service';
import { Util } from '../../../../components/shared/util';
import { AppState } from '../../../../core/core.state';
import { I18nService } from '../../../../i18n/i18n.service';
import { Subscriptions } from '../../../../util/Subscriptions';
import { actionLongdistanceRouteMapInit } from '../../../store/monitor.actions';
import { selectLongdistanceRouteMapPage } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteId } from '../../../store/monitor.selectors';
import { LongdistanceRouteMapService } from './longdistance-route-map.service';

@Component({
  selector: 'kpn-longdistance-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-longdistance-route-page-header
      pageName="map"
      [routeId]="routeId$ | async"
    ></kpn-longdistance-route-page-header>
    <div id="monitor-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="mapLayers"></kpn-layer-switcher>
    </div>
  `,
})
export class LongdistanceRouteMapComponent
  implements OnInit, AfterViewInit, OnDestroy {
  readonly routeId$ = this.store.select(selectLongdistanceRouteId);
  readonly response$ = this.store.select(selectLongdistanceRouteMapPage);

  mapLayers: MapLayers;
  map: Map;

  private readonly subscriptions = new Subscriptions();

  constructor(
    private i18nService: I18nService,
    private pageService: PageService,
    private mapService: LongdistanceRouteMapService,
    private store: Store<AppState>
  ) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.store.dispatch(actionLongdistanceRouteMapInit());
  }

  ngAfterViewInit(): void {
    this.subscriptions.add(
      this.response$.pipe(filter((x) => x != null)).subscribe((response) => {
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

        this.map.getView().fit(Util.toExtent(response.result.bounds, 0.05));

        this.subscriptions.add(
          this.pageService.sidebarOpen.subscribe((state) => {
            if (this.map) {
              setTimeout(() => this.map.updateSize(), 250);
            }
          })
        );
      })
    );
  }

  ngOnDestroy(): void {
    this.mapService.setMap(null);
    this.subscriptions.unsubscribe();
    this.pageService.showFooter = true;
    if (this.map) {
      this.map.dispose();
      this.map.setTarget(null);
    }
  }
}
