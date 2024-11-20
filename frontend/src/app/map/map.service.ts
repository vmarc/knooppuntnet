import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { LatLonImpl } from '@api/common';
import { OlUtil } from '@app/ol';
import { ZoomLevel } from '@app/ol/domain';
import { Layers } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { MVT } from 'ol/format';
import TileLayer from 'ol/layer/Tile';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
import OSM from 'ol/source/OSM';
import TileDebug from 'ol/source/TileDebug';
import VectorTile from 'ol/source/VectorTile';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { StyleFunction } from 'ol/style/Style';
import { createXYZ } from 'ol/tilegrid';
import View from 'ol/View';

@Injectable()
export class MapService {
  private _map: Map;
  private selectedRoute = signal<number | null>(null);

  private readonly unselectedRouteStyle = new Style({
    stroke: new Stroke({
      color: [130, 130, 130],
      width: 3,
    }),
  });

  private readonly selectedRouteStyle = new Style({
    stroke: new Stroke({
      color: [0, 0, 255],
      width: 3,
    }),
  });

  private readonly internationalRouteStyle = new Style({
    stroke: new Stroke({
      color: [255, 0, 0],
      width: 2,
    }),
  });
  private readonly nationalRouteStyle = new Style({
    stroke: new Stroke({
      color: [0, 0, 255],
      width: 3,
    }),
  });
  private readonly regionalRouteStyle = new Style({
    stroke: new Stroke({
      color: [0, 255, 0],
      width: 3,
    }),
  });

  private readonly localRouteStyle = new Style({
    stroke: new Stroke({
      color: [255, 255, 0],
      width: 3,
    }),
  });

  private readonly osmLayer = new TileLayer({
    source: new OSM({
      url: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
    }),
  });

  private readonly source = new VectorTile({
    tileSize: 512,
    minZoom: 6, // ZoomLevel.vectorTileMinZoom,
    maxZoom: ZoomLevel.vectorTileMaxZoom,
    format: new MVT(),
    url: `/tiles/hiking/{z}/{x}/{y}.mvt`,
  });

  private readonly layer = new VectorTileLayer({
    zIndex: Layers.zIndexNetworkLayer,
    source: this.source,
    renderMode: 'vector',
    style: this.styleFunction(),
  });

  private readonly tileGrid = createXYZ({
    tileSize: 512, // <--
    maxZoom: 20,
  });

  private readonly tileGridLayer = new TileLayer({
    source: new TileDebug({
      // zDirection: 1,
      tileGrid: this.tileGrid,
    }),
  });

  constructor() {
    console.log('MapService constructor');
  }

  init(): void {
    this._map = new Map({
      target: 'main-map',
      layers: [this.osmLayer, this.layer, this.tileGridLayer],
      controls: MapControls.build(),
      view: new View({
        minZoom: 6, //ZoomLevel.minZoom,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom, //ZoomLevel.maxZoom,
        zoom: 6,
      }),
    });

    const essen: LatLonImpl = { latitude: '51.46774', longitude: '4.46839' };
    const center = OlUtil.latLonToCoordinate(essen);
    this._map.getView().setCenter(center);
    // this._map.getView().setZoom(14);
  }

  destroy(): void {
    if (this._map) {
      // this._map.getView().un('change:resolution', this.updatePositionHandler);
      // this._map.getView().un('change:center', this.updatePositionHandler);
      this._map.dispose();
      this._map.setTarget(null);
    }
  }

  selectRoute(routeId: number) {
    this.selectedRoute.set(routeId);
    this.layer.changed();
  }

  styleFunction(): StyleFunction {
    return (feature, resolution) => {
      const routeId = feature.get('routeId');
      const name = feature.get('name');
      const layer = feature.get('layer');
      const surface = feature.get('surface');
      const segmentId = feature.get('segmentId');
      const segmentElementId = feature.get('segmentElementId');
      const pathIds = feature.get('pathIds');
      const scope = feature.get('layer');
      const colorName = feature.get('color');

      // console.log(
      //   `feature routeId=${routeId}, name=${name}, layer=${layer}, surface=${surface}, segmentId=${segmentId}, segmentElementId=${segmentElementId}, pathIds=${pathIds}`
      // );

      let style: Style = null;
      if (scope == 'international') {
        style = this.internationalRouteStyle;
      } else if (scope == 'national') {
        style = this.nationalRouteStyle;
      } else if (scope == 'regional') {
        style = this.regionalRouteStyle;
      } else {
        style = this.localRouteStyle;
      }

      // style = new Style({
      //   stroke: new Stroke({
      //     color: colorName,
      //     width: 3,
      //   }),
      // });

      // if (this.selectedRoute()) {
      //   if (routeId && routeId == this.selectedRoute()) {
      //     style = this.selectedRouteStyle;
      //   } else {
      //     style = this.unselectedRouteStyle;
      //   }
      // } else {
      //   style = this.selectedRouteStyle;
      // }
      return style;
    };
  }
}
