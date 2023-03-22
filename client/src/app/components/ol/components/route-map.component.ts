import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { RouteMapInfo } from '@api/common/route/route-map-info';
import { List } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import { Extent } from 'ol/extent';
import Map from 'ol/Map';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import { fromEvent } from 'rxjs';
import { Subscriptions } from '../../../util/Subscriptions';
import { PageService } from '../../shared/page.service';
import { Util } from '../../shared/util';
import { MapPosition } from '../domain/map-position';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { MapClickService } from '../services/map-click.service';
import { MapLayerService } from '../services/map-layer.service';
import { MapService } from '../services/map.service';
import { OldMapPositionService } from '../services/old-map-position.service';

@Component({
  selector: 'kpn-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="route-map" class="kpn-map">
      <kpn-old-layer-switcher [mapLayers]="layers"/>
      <kpn-map-link-menu [map]="map"/>
    </div>
  `,
})
export class RouteMapComponent implements AfterViewInit, OnDestroy {
  @Input() routeMapInfo: RouteMapInfo;
  @Input() mapPositionFromUrl: MapPosition;

  map: Map;
  layers: MapLayers;
  private networkVectorTileLayer: MapLayer;
  private networkVectorTileLayerActive = true;

  private readonly mapId = 'route-map';
  private readonly subscriptions = new Subscriptions();

  constructor(
    private mapService: MapService,
    private mapClickService: MapClickService,
    private mapLayerService: MapLayerService,
    private pageService: PageService,
    private mapPositionService: OldMapPositionService
  ) {}

  ngAfterViewInit(): void {
    this.layers = this.buildLayers();
    setTimeout(
      () => this.mapLayerService.restoreMapLayerStates(this.layers),
      0
    );

    let viewOptions: ViewOptions = {
      minZoom: ZoomLevel.minZoom,
      maxZoom: ZoomLevel.maxZoom,
    };

    if (this.mapPositionFromUrl) {
      const center: Coordinate = [
        this.mapPositionFromUrl.x,
        this.mapPositionFromUrl.y,
      ];
      const zoom = this.mapPositionFromUrl.zoom;
      viewOptions = {
        ...viewOptions,
        center,
        zoom,
      };
    }

    this.map = new Map({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View(viewOptions),
    });

    this.mapClickService.installOn(this.map);

    const view = this.map.getView();

    if (!this.mapPositionFromUrl) {
      view.fit(this.buildExtent());
    }

    view.on('change:resolution', () => {
      if (view.getZoom() < ZoomLevel.vectorTileMinZoom) {
        if (this.networkVectorTileLayerActive) {
          this.map.removeLayer(this.networkVectorTileLayer.layer);
          this.networkVectorTileLayerActive = false;
        }
      } else {
        if (this.networkVectorTileLayerActive === false) {
          this.map.getLayers().insertAt(1, this.networkVectorTileLayer.layer);
          this.networkVectorTileLayerActive = true;
        }
      }
    });

    this.subscriptions.add(
      this.pageService.sidebarOpen.subscribe(() => this.updateSize())
    );
    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() =>
        this.updateSize()
      )
    );

    this.mapPositionService.install(this.map.getView());
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

  private buildExtent(): Extent {
    const bounds = this.routeMapInfo.map.bounds;
    const min = Util.toCoordinate(bounds.latMin, bounds.lonMin);
    const max = Util.toCoordinate(bounds.latMax, bounds.lonMax);
    return [min[0], min[1], max[0], max[1]];
  }

  private buildLayers(): MapLayers {
    this.networkVectorTileLayer = this.mapLayerService.networkVectorTileLayer(
      this.routeMapInfo.networkType
    );
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(
      this.mapLayerService.backgroundLayer(this.mapId)
    );
    mapLayers = mapLayers.push(this.networkVectorTileLayer);
    mapLayers = mapLayers.concat(
      this.mapLayerService.routeLayers(this.routeMapInfo.map)
    );
    mapLayers = mapLayers.push(this.mapLayerService.tile256NameLayer());
    return new MapLayers(mapLayers);
  }
}
