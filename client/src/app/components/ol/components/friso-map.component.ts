import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { NetworkType } from '@api/custom/network-type';
import { FrisoNode } from '@app/components/ol/components/friso-node';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { MapService } from '@app/components/ol/services/map.service';
import { MainMapStyle } from '@app/components/ol/style/main-map-style';
import { MainMapStyleParameters } from '@app/components/ol/style/main-map-style-parameters';
import { selectFrisoMode } from '@app/friso/store/friso.selectors';
import { Subscriptions } from '@app/util/Subscriptions';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import { MapBrowserEvent } from 'ol';
import { FeatureLike } from 'ol/Feature';
import Interaction from 'ol/interaction/Interaction';
import Map from 'ol/Map';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import View from 'ol/View';
import { Observable } from 'rxjs';
import { fromEvent } from 'rxjs';
import { PageService } from '../../shared/page.service';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayers } from '../layers/map-layers';
import { MapLayerService } from '../services/map-layer.service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';

@Component({
  selector: 'kpn-friso-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="friso-map" class="kpn-map">
      <kpn-old-layer-switcher [mapLayers]="switcherLayers" />
    </div>
  `,
})
export class FrisoMapComponent implements AfterViewInit, OnDestroy {
  bounds: Bounds = {
    minLat: 50.92176250622567,
    minLon: 3.5257314989690713,
    maxLat: 53.28321294207922,
    maxLon: 6.729255052272727,
  };
  @Output() nodeClicked = new EventEmitter<FrisoNode>();

  switcherLayers: MapLayers;
  layers: MapLayers;
  private map: Map;

  private readonly mapId = 'friso-map';
  private readonly subscriptions = new Subscriptions();

  constructor(
    private mapService: MapService,
    private mapLayerService: MapLayerService,
    private pageService: PageService,
    private store: Store
  ) {}

  ngAfterViewInit(): void {
    this.mapService.nextNetworkType(NetworkType.hiking);
    this.layers = this.buildLayers();

    this.switcherLayers = new MapLayers(
      this.layers.layers.filter(
        (mapLayer) =>
          mapLayer.name === 'osm-layer' ||
          mapLayer.name === 'background-layer' ||
          mapLayer.name === 'network-hiking-layer'
      )
    );

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
        maxZoom: ZoomLevel.maxZoom,
      }),
    });

    const view = this.map.getView();
    view.fit(Util.toExtent(this.bounds, 0.1));

    this.map.addInteraction(this.buildInteraction());

    this.subscriptions.add(
      this.pageService.sidebarOpen.subscribe(() => this.updateSize())
    );
    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() =>
        this.updateSize()
      )
    );

    this.subscriptions.add(
      this.store.select(selectFrisoMode).subscribe((mode) => {
        const layerName = `friso-${mode}-layer`;
        this.layers.layers.forEach((layer) => {
          if (layer.name.startsWith('friso-')) {
            const visible = layer.name === layerName;
            layer.layer.setVisible(visible);
          }
        });
      })
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

    return new MapLayers(
      List([
        new OsmLayer().build(),
        new BackgroundLayer().build(),
        this.mapLayerService.mainMapVectorLayer(
          NetworkType.hiking,
          mainMapStyle.styleFunction()
        ),
        this.mapLayerService.mainMapBitmapLayer(NetworkType.hiking),
        this.mapLayerService.frisoLayer('rename'), //'Renamed_ext.geojson'
        this.mapLayerService.frisoLayer('minor-rename'), // Minor rename_ext.geojson
        this.mapLayerService.frisoLayer('removed'), // Removed_osm.geojson
        this.mapLayerService.frisoLayer('added'), // Added_ext.geojson
        this.mapLayerService.frisoLayer('no-change'), // No change_ext.geojson
        this.mapLayerService.frisoLayer('moved-short-distance'), // Moved short distance_ext.geojson
        this.mapLayerService.frisoLayer('moved-long-distance'), // Moved long distance_ext.geojson
        this.mapLayerService.frisoLayer('other'), // other.geojson
      ])
    );
  }

  private buildInteraction(): Interaction {
    return new Interaction({
      handleEvent: (event: MapBrowserEvent<MouseEvent>) => {
        if (MapBrowserEventType.SINGLECLICK === event.type) {
          return this.handleSingleClickEvent(event);
        }
        if (MapBrowserEventType.POINTERMOVE === event.type) {
          return this.handleMoveEvent(event);
        }
        return true; // propagate event
      },
    });
  }

  private handleSingleClickEvent(evt: MapBrowserEvent<MouseEvent>): boolean {
    const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {
      hitTolerance: 10,
    });
    if (features) {
      const index = features.findIndex(
        (feature) => !!feature.get('distance closest node')
      );
      if (index >= 0) {
        const name = features[index].get('rwn_ref');
        const distanceClosestNode = features[index].get(
          'distance closest node'
        );
        const node = new FrisoNode(name, Math.round(+distanceClosestNode));
        this.nodeClicked.emit(node);
        return false; // do not propagate event
      }
    }
    return true; // propagate event
  }

  private handleMoveEvent(evt: MapBrowserEvent<MouseEvent>): boolean {
    const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {
      hitTolerance: 10,
    });
    if (features && features.length > 0) {
      const index = features.findIndex((feature) => !!feature.get('rwn_ref'));
      evt.map.getTargetElement().style.cursor =
        index >= 0 ? 'pointer' : 'default';
    }
    return true; // propagate event
  }
}
