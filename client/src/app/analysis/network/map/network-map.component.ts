import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { NetworkMapPage } from '@api/common/network/network-map-page';
import { NetworkNodesBitmapTileLayer } from '@app/components/ol/layers/network-nodes-bitmap-tile-layer';
import { NetworkNodesVectorTileLayer } from '@app/components/ol/layers/network-nodes-vector-tile-layer';
import { List } from 'immutable';
import View from 'ol/View';
import { Util } from '@app/components/shared/util';
import { NetworkMapPosition } from '@app/components/ol/domain/network-map-position';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { OldMapLayers } from '@app/components/ol/layers/old-map-layers';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';
import { MapZoomService } from '@app/components/ol/services/map-zoom.service';
import { NetworkMapPositionService } from '@app/components/ol/services/network-map-position.service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { NetworkNodesMarkerLayer } from '@app/components/ol/layers/network-nodes-marker-layer';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { NewMapService } from '@app/components/ol/services/new-map.service';

@Component({
  selector: 'kpn-network-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-map">
      <kpn-network-control (action)="zoomInToNetwork()" />
      <kpn-old-layer-switcher [mapLayers]="layers" />
      <kpn-old-map-link-menu [map]="map" />
    </div>
  `,
})
export class NetworkMapComponent implements AfterViewInit, OnDestroy {
  @Input() networkId: number;
  @Input() page: NetworkMapPage;
  @Input() mapPositionFromUrl: NetworkMapPosition;

  protected map: OpenLayersMap;
  protected layers: OldMapLayers;
  protected readonly mapId = 'network-nodes-map';

  constructor(
    private newMapService: NewMapService,
    private mapLayerService: MapLayerService,
    private mapClickService: MapClickService,
    private mapZoomService: MapZoomService,
    private networkMapPositionService: NetworkMapPositionService
  ) {}

  ngAfterViewInit(): void {
    this.layers = this.buildLayers();
    setTimeout(
      () => this.mapLayerService.restoreMapLayerStates(this.layers),
      0
    );
    this.map = this.newMapService.build({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
      }),
    });

    const view = this.map.map.getView();
    this.networkMapPositionService.install(
      view,
      this.networkId,
      this.page.bounds,
      this.mapPositionFromUrl
    );
    this.mapZoomService.install(view);
    this.mapClickService.installOn(this.map.map);
  }

  ngOnDestroy(): void {
    this.map.destroy();
  }

  zoomInToNetwork(): void {
    const extent = Util.toExtent(this.page.bounds, 0.1);
    this.map.map.getView().fit(extent);
  }

  private buildLayers(): OldMapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(BackgroundLayer.build());
    mapLayers = mapLayers.push(
      NetworkNodesBitmapTileLayer.build(this.page.summary.networkType)
    );
    mapLayers = mapLayers.push(
      NetworkNodesVectorTileLayer.build(
        this.page.summary.networkType,
        this.page.nodeIds,
        this.page.routeIds
      )
    );
    mapLayers = mapLayers.push(
      new NetworkNodesMarkerLayer().build(this.page.nodes)
    );
    mapLayers = mapLayers.push(new TileDebug256Layer().build());
    return new OldMapLayers(mapLayers);
  }
}
