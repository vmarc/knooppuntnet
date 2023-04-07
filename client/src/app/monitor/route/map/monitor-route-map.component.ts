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
import { OldMapLayers } from '@app/components/ol/layers/old-map-layers';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { Util } from '@app/components/shared/util';
import { selectQueryParam } from '@app/core/core.state';
import { I18nService } from '@app/i18n/i18n.service';
import { Subscriptions } from '@app/util/Subscriptions';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import View from 'ol/View';
import { distinct } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { MonitorRouteMapService } from './monitor-route-map.service';
import { actionMonitorRouteMapPageDestroy } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapPositionChanged } from './store/monitor-route-map.actions';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { NewMapService } from '@app/components/ol/services/new-map.service';

@Component({
  selector: 'kpn-monitor-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-map">
      <kpn-old-layer-switcher [mapLayers]="mapLayers" />
      <kpn-map-link-menu [map]="map" />
    </div>
  `,
})
export class MonitorRouteMapComponent
  implements OnInit, AfterViewInit, OnDestroy
{
  @Input() page: MonitorRouteMapPage;

  protected map: OpenLayersMap;
  protected readonly mapId = 'monitor-map';
  protected mapLayers: OldMapLayers;
  private mapPositionFromUrl: MapPosition;
  private updatePositionHandler = () => this.updateMapPosition();
  private currentMapPosition$ = new BehaviorSubject<MapPosition>(null);

  private readonly subscriptions = new Subscriptions();

  constructor(
    private newMapService: NewMapService,
    private i18nService: I18nService,
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
    const osmLayer = OsmLayer.build();
    osmLayer.layer.setVisible(false);
    layers.push(osmLayer);

    const backgroundLayer = BackgroundLayer.build();
    backgroundLayer.layer.setVisible(true);
    layers.push(backgroundLayer);

    this.mapLayers = new OldMapLayers(List(layers));

    this.map = this.newMapService.build({
      target: this.mapId,
      layers: this.mapLayers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: 0,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom,
      }),
    });

    this.mapService.setMap(this.map.map);

    this.mapService.layers().forEach((layer) => {
      this.map.map.addLayer(layer);
    });

    if (this.mapPositionFromUrl) {
      this.map.map.getView().setZoom(this.mapPositionFromUrl.zoom);
      this.map.map.getView().setRotation(this.mapPositionFromUrl.rotation);
      const center: Coordinate = [
        this.mapPositionFromUrl.x,
        this.mapPositionFromUrl.y,
      ];
      this.map.map.getView().setCenter(center);
    } else {
      this.map.map.getView().fit(Util.toExtent(this.page.bounds, 0.05));
    }

    this.map.map.getView().on('change:resolution', this.updatePositionHandler);
    this.map.map.getView().on('change:center', this.updatePositionHandler);
  }

  ngOnDestroy(): void {
    this.map.map.getView().un('change:resolution', this.updatePositionHandler);
    this.map.map.getView().un('change:center', this.updatePositionHandler);
    this.mapService.setMap(null);
    this.subscriptions.unsubscribe();
    this.map.destroy();
    this.store.dispatch(actionMonitorRouteMapPageDestroy());
  }

  private updateMapPosition(): void {
    const center: Coordinate = this.map.map.getView().getCenter();
    if (center) {
      const zoom = this.map.map.getView().getZoom();
      const z = Math.round(zoom);
      const mapPosition = new MapPosition(z, center[0], center[1], 0);
      this.currentMapPosition$.next(mapPosition);
    }
  }
}
