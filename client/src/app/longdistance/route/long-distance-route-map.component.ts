import {OnDestroy} from '@angular/core';
import {AfterViewInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {List} from 'immutable';
import Map from 'ol/Map';
import View from 'ol/View';
import {filter} from 'rxjs/operators';
import {ZoomLevel} from '../../components/ol/domain/zoom-level';
import {BackgroundLayer} from '../../components/ol/layers/background-layer';
import {MapControls} from '../../components/ol/layers/map-controls';
import {MapLayer} from '../../components/ol/layers/map-layer';
import {MapLayers} from '../../components/ol/layers/map-layers';
import {OsmLayer} from '../../components/ol/layers/osm-layer';
import {PageService} from '../../components/shared/page.service';
import {Util} from '../../components/shared/util';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRouteMapFocus} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMap} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteId} from '../../core/longdistance/long-distance.selectors';
import {I18nService} from '../../i18n/i18n.service';
import {Subscriptions} from '../../util/Subscriptions';
import {LongDistanceRouteMapService} from './long-distance-route-map.service';

@Component({
  selector: 'kpn-long-distance-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-long-distance-route-page-header pageName="map" [routeId]="routeId$ | async"></kpn-long-distance-route-page-header>
    <div id="long-distance-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="mapLayers"></kpn-layer-switcher>
    </div>
  `
})
export class LongDistanceRouteMapComponent implements AfterViewInit, OnDestroy {

  routeId$ = this.store.select(selectLongDistanceRouteId);
  response$ = this.store.select(selectLongDistanceRouteMap);

  mapLayers: MapLayers;
  map: Map;

  private readonly subscriptions = new Subscriptions();

  constructor(private i18nService: I18nService,
              private pageService: PageService,
              private mapService: LongDistanceRouteMapService,
              private store: Store<AppState>) {
    this.pageService.showFooter = false;
  }

  ngAfterViewInit(): void {

    this.subscriptions.add(
      this.response$.pipe(filter(x => x != null)).subscribe(response => {

        const layers: MapLayer[] = [];
        const osmLayer = new OsmLayer(this.i18nService).build();
        osmLayer.layer.setVisible(false);
        layers.push(osmLayer);

        const backgroundLayer = new BackgroundLayer(this.i18nService).build('long-distance-map');
        backgroundLayer.layer.setVisible(true);
        layers.push(backgroundLayer);

        this.mapLayers = new MapLayers(List(layers));

        this.map = new Map({
          target: 'long-distance-map',
          layers: this.mapLayers.toArray(),
          controls: MapControls.build(),
          view: new View({
            minZoom: 0,
            maxZoom: ZoomLevel.vectorTileMaxOverZoom
          })
        });

        this.mapService.layers().forEach(layer => {
          this.map.addLayer(layer);
          setTimeout(() => layer.changed(), 1000);
        });

        this.map.getView().fit(Util.toExtent(response.result.bounds, 0.05));

        this.subscriptions.add(
          this.store.select(selectLongDistanceRouteMapFocus).subscribe(bounds => {
            if (bounds) {
              this.map.getView().fit(Util.toExtent(bounds, 0.1));
            }
          })
        );
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.pageService.showFooter = true;
    if (this.map) {
      this.map.dispose();
      this.map.setTarget(null);
    }
  }

}
