import { Input } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { List } from 'immutable';
import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import View from 'ol/View';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { OldMapLayers } from '@app/components/ol/layers/old-map-layers';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { Util } from '@app/components/shared/util';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { NewMapService } from '@app/components/ol/services/new-map.service';

@Component({
  selector: 'kpn-monitor-route-change-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-embedded-map">
      <kpn-old-layer-switcher [mapLayers]="mapLayers"></kpn-old-layer-switcher>
    </div>
  `,
})
export class MonitorRouteChangeMapComponent
  implements AfterViewInit, OnDestroy
{
  @Input() mapId: string;
  @Input() referenceJson: string;
  @Input() routeSegments: MonitorRouteSegment[];
  @Input() deviation: MonitorRouteDeviation;

  protected mapLayers: OldMapLayers;
  protected map: OpenLayersMap;

  constructor(private newMapService: NewMapService) {}

  ngAfterViewInit(): void {
    const layers: MapLayer[] = [];
    const osmLayer = OsmLayer.build();
    osmLayer.layer.setVisible(false);
    layers.push(osmLayer);

    const backgroundLayer = BackgroundLayer.build();
    backgroundLayer.layer.setVisible(true);
    layers.push(backgroundLayer);

    layers.push(this.buildReferenceLayer());
    layers.push(this.buildNokSegmentLayer());
    layers.push(this.buildOsmRelationLayer());

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

    this.map.map.getView().fit(Util.toExtent(this.deviation.bounds, 0.05));
  }

  ngOnDestroy(): void {
    this.map.destroy();
  }

  private buildReferenceLayer(): MapLayer {
    const layerStyle = this.fixedStyle('blue', 4);
    const features = new GeoJSON().readFeatures(this.referenceJson, {
      featureProjection: 'EPSG:3857',
    });
    const layer = new VectorLayer({
      zIndex: 50,
      source: new VectorSource({ features }),
      style: () => layerStyle,
    });

    layer.set('name', 'GPX reference'); // TODO planner: set elsewhere?
    return MapLayer.simpleLayer('gpx-reference-layer', layer);
  }

  private buildNokSegmentLayer(): MapLayer {
    const layerStyle = this.fixedStyle('red', 4);
    const features = new GeoJSON().readFeatures(this.deviation.geoJson, {
      featureProjection: 'EPSG:3857',
    });

    const layer = new VectorLayer({
      zIndex: 70,
      source: new VectorSource({ features }),
      style: () => layerStyle,
    });
    layer.set('name', 'Not OK segment'); // TODO planner: set elsewhere?
    return MapLayer.simpleLayer('not-ok-layer', layer);
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
      style: () => thickStyle,
    });
    layer.set('name', 'OSM Relation'); // TODO planner: set elsewhere?
    return MapLayer.simpleLayer('osm-relation-layer', layer);
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
