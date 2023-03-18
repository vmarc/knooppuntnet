import { OnInit } from '@angular/core';
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
import { selectQueryParam } from '@app/core/core.state';
import { I18nService } from '@app/i18n/i18n.service';
import { Subscriptions } from '@app/util/Subscriptions';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import View from 'ol/View';
import { distinct } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { fromEvent } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { MapPosition } from '../../../components/ol/domain/map-position';
import { MonitorRouteMapService } from './monitor-route-map.service';
import { actionMonitorRouteMapPageDestroy } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapPositionChanged } from './store/monitor-route-map.actions';

@Component({
  selector: 'kpn-monitor-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="monitor-map" class="kpn-map">
      <kpn-old-layer-switcher [mapLayers]="mapLayers"/>
      <kpn-map-link-menu [map]="map"/>
    </div>
  `,
})
export class MonitorRouteMapComponent
  implements OnInit, AfterViewInit, OnDestroy
{
  @Input() page: MonitorRouteMapPage;

  map: Map;
  mapLayers: MapLayers;
  private mapPositionFromUrl: MapPosition;
  private updatePositionHandler = () => this.updateMapPosition();
  private currentMapPosition$ = new BehaviorSubject<MapPosition>(null);

  private readonly subscriptions = new Subscriptions();

  constructor(
    private i18nService: I18nService,
    private pageService: PageService,
    private mapService: MonitorRouteMapService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.currentMapPosition$
        .pipe(distinct(), debounceTime(50))
        .subscribe((mapPosition) => {
          if (mapPosition) {
            this.store.dispatch(
              actionMonitorRouteMapPositionChanged({ mapPosition })
            );
          }
        }),
      this.store.select(selectQueryParam('position')).subscribe((mapString) => {
        this.mapPositionFromUrl = MapPosition.fromQueryParam(mapString);
      })
    );
  }

  ngAfterViewInit(): void {
    const layers: MapLayer[] = [];
    const osmLayer = new OsmLayer().build();
    osmLayer.layer.setVisible(false);
    layers.push(osmLayer);

    const backgroundLayer = new BackgroundLayer().build('monitor-map');
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

    if (this.mapPositionFromUrl) {
      this.map.getView().setZoom(this.mapPositionFromUrl.zoom);
      this.map.getView().setRotation(this.mapPositionFromUrl.rotation);
      const center: Coordinate = [
        this.mapPositionFromUrl.x,
        this.mapPositionFromUrl.y,
      ];
      this.map.getView().setCenter(center);
    } else {
      this.map.getView().fit(Util.toExtent(this.page.bounds, 0.05));
    }

    this.subscriptions.add(
      this.pageService.sidebarOpen.subscribe(() => this.updateSize())
    );
    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() =>
        this.updateSize()
      )
    );
    this.map.getView().on('change:resolution', this.updatePositionHandler);
    this.map.getView().on('change:center', this.updatePositionHandler);
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
    this.map.getView().un('change:resolution', this.updatePositionHandler);
    this.map.getView().un('change:center', this.updatePositionHandler);
    this.mapService.setMap(null);
    this.subscriptions.unsubscribe();
    if (this.map) {
      this.map.dispose();
      this.map.setTarget(null);
    }
    this.store.dispatch(actionMonitorRouteMapPageDestroy());
  }

  private updateMapPosition(): void {
    const center: Coordinate = this.map.getView().getCenter();
    if (center) {
      const zoom = this.map.getView().getZoom();
      const z = Math.round(zoom);
      const mapPosition = new MapPosition(z, center[0], center[1], 0);
      this.currentMapPosition$.next(mapPosition);
    }
  }
}
