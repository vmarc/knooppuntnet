import {OnDestroy} from '@angular/core';
import {AfterViewInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {List} from 'immutable';
import {GeoJSON} from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import {Style} from 'ol/style';
import {Stroke} from 'ol/style';
import View from 'ol/View';
import {filter} from 'rxjs/operators';
import {ZoomLevel} from '../../components/ol/domain/zoom-level';
import {MapControls} from '../../components/ol/layers/map-controls';
import {MapLayer} from '../../components/ol/layers/map-layer';
import {MapLayers} from '../../components/ol/layers/map-layers';
import {OsmLayer} from '../../components/ol/layers/osm-layer';
import {PageService} from '../../components/shared/page.service';
import {Util} from '../../components/shared/util';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRouteMap} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteId} from '../../core/longdistance/long-distance.selectors';
import {I18nService} from '../../i18n/i18n.service';

@Component({
  selector: 'kpn-long-distance-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-long-distance-route-page-header pageName="map" [routeId]="routeId$ | async"></kpn-long-distance-route-page-header>

    <!--    <div *ngIf="response$ | async as response" class="kpn-spacer-above">-->
    <!--      <div *ngIf="!response.result">-->
    <!--        Route not found-->
    <!--      </div>-->
    <!--      <div *ngIf="response.result">-->
    <!--        Not implemented yet-->
    <!--      </div>-->
    <!--    </div>-->
    <div id="long-distance-map" class="map">
      <kpn-layer-switcher [mapLayers]="mapLayers"></kpn-layer-switcher>
    </div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 48px;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: white;
      overflow: hidden;
    }
  `]
})
export class LongDistanceRouteMapComponent implements AfterViewInit, OnDestroy {

  routeId$ = this.store.select(selectLongDistanceRouteId);
  response$ = this.store.select(selectLongDistanceRouteMap);

  mapLayers: MapLayers;
  map: Map;

  constructor(private i18nService: I18nService,
              private pageService: PageService,
              private store: Store<AppState>) {
    this.pageService.showFooter = false;
  }

  ngAfterViewInit(): void {

    this.response$.pipe(filter(x => x != null)).subscribe(response => {

      const layers: MapLayer[] = [];
      const osmLayer = new OsmLayer(this.i18nService).build();
      osmLayer.layer.setVisible(true);
      layers.push(osmLayer);

      if (response.result.gpxGeometry) {
        layers.push(this.geojsonLayer('GPX', 'rgba(0, 0, 255, 0.9)', response.result.gpxGeometry));
      }

      if (response.result.osmSegments) {
        response.result.osmSegments.forEach(s => {
          layers.push(this.geojsonLayer('OSM', 'rgba(255, 255, 0, 0.9)', s.geoJson));
        });
      }

      if (response.result.okGeometry) {
        layers.push(this.geojsonLayer('OK', 'rgba(0, 255, 0, 0.9)', response.result.okGeometry));
      }

      if (response.result.nokGeometry) {
        layers.push(this.geojsonLayer('NOK', 'rgba(255, 0, 0, 0.9)', response.result.nokGeometry));
      }

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

      this.map.getView().fit(Util.toExtent(response.result.bounds, 0.05));
    });
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }

  private geojsonLayer(name: string, color: string, geoJson: string): MapLayer {
    const features = new GeoJSON().readFeatures(geoJson, {featureProjection: 'EPSG:3857'});

    const vectorSource = new VectorSource({
      features
    });

    const locationStyle = new Style({
      stroke: new Stroke({
        color,
        width: 4
      })
    });

    const styleFunction = function (feature) {
      return locationStyle;
    };

    const layer = new VectorLayer({
      source: vectorSource,
      style: styleFunction
    });

    layer.set('name', name);
    layer.setVisible(true);
    return new MapLayer(name, layer);
  }

}
