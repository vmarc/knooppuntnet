import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { NodeMapInfo } from '@api/common/node-map-info';
import { selectPreferencesNetworkType } from '@app/core/preferences/preferences.selectors';
import { Subscriptions } from '@app/util/Subscriptions';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import { fromEvent } from 'rxjs';
import { Observable } from 'rxjs';
import { take } from 'rxjs/operators';
import { PageService } from '../../shared/page.service';
import { Util } from '../../shared/util';
import { MapPosition } from '../domain/map-position';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { MapClickService } from '../services/map-click.service';
import { MapLayerService } from '../services/map-layer.service';
import { OldMapPositionService } from '../services/old-map-position.service';

@Component({
  selector: 'kpn-node-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="node-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="layers"/>
      <kpn-map-link-menu [map]="map"/>
    </div>
  `,
})
export class NodeMapComponent implements AfterViewInit, OnDestroy {
  @Input() nodeMapInfo: NodeMapInfo;
  @Input() mapPositionFromUrl: MapPosition;

  map: Map;
  layers: MapLayers;
  private readonly mapId = 'node-map';
  private readonly subscriptions = new Subscriptions();

  private readonly defaultNetworkType$: Observable<string> = this.store.select(
    selectPreferencesNetworkType
  );

  constructor(
    private mapClickService: MapClickService,
    private mapLayerService: MapLayerService,
    private mapPositionService: OldMapPositionService,
    private pageService: PageService,
    private store: Store
  ) {}

  ngAfterViewInit(): void {
    this.layers = this.buildLayers();
    setTimeout(
      () => this.mapLayerService.restoreMapLayerStates(this.layers),
      0
    );

    let viewOptions: ViewOptions = {
      minZoom: ZoomLevel.vectorTileMinZoom,
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
    } else {
      const center = Util.toCoordinate(
        this.nodeMapInfo.latitude,
        this.nodeMapInfo.longitude
      );
      viewOptions = {
        ...viewOptions,
        center,
        zoom: 18,
      };
    }

    this.map = new Map({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View(viewOptions),
    });

    this.layers.applyMap(this.map);

    this.mapClickService.installOn(this.map);

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

  private buildLayers(): MapLayers {
    const networkLayers = this.mapLayerService.networkLayers(
      this.nodeMapInfo.networkTypes
    );
    if (networkLayers.length > 1) {
      this.defaultNetworkType$.pipe(take(1)).subscribe((defaultNetworkType) => {
        networkLayers.forEach((networkLayer) => {
          if (
            defaultNetworkType != null &&
            networkLayer.name !== defaultNetworkType
          ) {
            networkLayer.layer.setVisible(false);
          }
        });
      });
    }

    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(
      this.mapLayerService.backgroundLayer(this.mapId)
    );
    mapLayers = mapLayers.concat(networkLayers);
    mapLayers = mapLayers.push(
      this.mapLayerService.nodeMarkerLayer(this.nodeMapInfo)
    );
    mapLayers = mapLayers.push(this.mapLayerService.tile256NameLayer());
    return new MapLayers(mapLayers);
  }
}
