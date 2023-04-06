import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { NodeMapInfo } from '@api/common/node-map-info';
import { Subscriptions } from '@app/util/Subscriptions';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import { PageService } from '@app/components/shared/page.service';
import { Util } from '@app/components/shared/util';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';
import { OldMapPositionService } from '@app/components/ol/services/old-map-position.service';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { actionNodeMapLayerVisible } from '@app/analysis/node/store/node.actions';
import { fromEvent } from 'rxjs';

@Component({
  selector: 'kpn-node-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="node-map" class="kpn-map">
      <kpn-layer-switcher
        [layerStates]="layerStates"
        (layerStateChange)="layerStateChange($event)"
      />
      <kpn-map-link-menu [map]="map" />
    </div>
  `,
})
export class NodeMapComponent implements AfterViewInit, OnDestroy {
  @Input() nodeMapInfo: NodeMapInfo;
  @Input() mapPositionFromUrl: MapPosition;
  @Input() layerStates: MapLayerState[];
  @Input() layers: MapLayer[];

  protected map: Map;

  private readonly mapId = 'node-map';
  private readonly subscriptions = new Subscriptions();

  constructor(
    private mapClickService: MapClickService,
    private mapLayerService: MapLayerService,
    private mapPositionService: OldMapPositionService,
    private pageService: PageService,
    private store: Store
  ) {}

  ngAfterViewInit(): void {
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
      layers: this.layers.map((mapLayer) => mapLayer.layer),
      controls: MapControls.build(),
      view: new View(viewOptions),
    });

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

  layerStateChange(change: MapLayerState): void {
    this.store.dispatch(actionNodeMapLayerVisible(change));
  }

  private updateSize(): void {
    if (this.map) {
      setTimeout(() => {
        this.map.updateSize();
      }, 0);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    if (this.map) {
      this.map.setTarget(null);
    }
  }
}
