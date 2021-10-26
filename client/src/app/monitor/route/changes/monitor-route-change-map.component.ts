import { Input } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteNokSegment } from '@api/common/monitor/monitor-route-nok-segment';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { List } from 'immutable';
import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import View from 'ol/View';
import { fromEvent } from 'rxjs';
import { ZoomLevel } from '../../../components/ol/domain/zoom-level';
import { BackgroundLayer } from '../../../components/ol/layers/background-layer';
import { MapControls } from '../../../components/ol/layers/map-controls';
import { MapLayer } from '../../../components/ol/layers/map-layer';
import { MapLayers } from '../../../components/ol/layers/map-layers';
import { OsmLayer } from '../../../components/ol/layers/osm-layer';
import { Util } from '../../../components/shared/util';
import { I18nService } from '../../../i18n/i18n.service';
import { Subscriptions } from '../../../util/Subscriptions';

@Component({
  selector: 'kpn-monitor-route-change-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-embedded-map">
      <kpn-layer-switcher [mapLayers]="mapLayers"></kpn-layer-switcher>
    </div>
  `,
})
export class MonitorRouteChangeMapComponent
  implements AfterViewInit, OnDestroy
{
  @Input() mapId: string;
  @Input() referenceJson: string;
  @Input() routeSegments: MonitorRouteSegment[];
  @Input() nokSegment: MonitorRouteNokSegment;

  mapLayers: MapLayers;
  map: Map;

  private readonly subscriptions = new Subscriptions();

  constructor(private i18nService: I18nService) {}

  ngAfterViewInit(): void {
    const layers: MapLayer[] = [];
    const osmLayer = new OsmLayer(this.i18nService).build();
    osmLayer.layer.setVisible(false);
    layers.push(osmLayer);

    const backgroundLayer = new BackgroundLayer(this.i18nService).build(
      this.mapId
    );
    backgroundLayer.layer.setVisible(true);
    layers.push(backgroundLayer);

    layers.push(this.buildReferenceLayer());
    layers.push(this.buildNokSegmentLayer());
    layers.push(this.buildOsmRelationLayer());

    this.mapLayers = new MapLayers(List(layers));

    this.map = new Map({
      target: this.mapId,
      layers: this.mapLayers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: 0,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom,
      }),
    });

    this.map.getView().fit(Util.toExtent(this.nokSegment.bounds, 0.05));
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
        this.mapLayers.updateSize();
      }, 0);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    if (this.map) {
      this.map.dispose();
      this.map.setTarget(null);
    }
  }

  private buildReferenceLayer(): MapLayer {
    const layerStyle = this.fixedStyle('blue', 4);
    const features = new GeoJSON().readFeatures(this.referenceJson, {
      featureProjection: 'EPSG:3857',
    });
    const layer = new VectorLayer({
      zIndex: 50,
      source: new VectorSource({ features }),
      style: (feature) => layerStyle,
    });

    layer.set('name', 'GPX reference');
    return new MapLayer('gpx-reference-layer', layer);
  }

  private buildNokSegmentLayer(): MapLayer {
    const layerStyle = this.fixedStyle('red', 4);
    const features = new GeoJSON().readFeatures(this.nokSegment.geoJson, {
      featureProjection: 'EPSG:3857',
    });

    const layer = new VectorLayer({
      zIndex: 70,
      source: new VectorSource({ features }),
      style: (feature) => layerStyle,
    });
    layer.set('name', 'Not OK segment');
    return new MapLayer('not-ok-layer', layer);
  }

  private buildOsmRelationLayer(): MapLayer {
    const thickStyle = this.fixedStyle('yellow', 10);
    const features = [];
    this.routeSegments.forEach((segment) => {
      new GeoJSON()
        .readFeatures(segment.geoJson, { featureProjection: 'EPSG:3857' })
        .forEach((feature) => {
          feature.set('segmentId', segment.id);
          features.push(feature);
        });
    });

    const layer = new VectorLayer({
      zIndex: 40,
      source: new VectorSource({ features }),
      style: (feature) => thickStyle,
    });
    layer.set('name', 'OSM Relation');
    return new MapLayer('osm-relation-layer', layer);
  }

  private fixedStyle(color: string, width: number): Style {
    return new Style({
      stroke: new Stroke({
        color,
        width,
      }),
    });
  }
}
