import { effect } from '@angular/core';
import { Signal } from '@angular/core';
import { ZoomLevel } from '@app/ol/domain';
import { OldLayers } from '@app/ol/layers';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { ExploreStyle } from '../style/explore-style';

export class RouteLayer {
  private zoom: number;

  constructor(zoomSignal: Signal<number>) {
    effect(() => {
      this.zoom = zoomSignal();
    });
  }

  build(): VectorTileLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: 6, // ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom,
      format: new MVT(),
      url: `/tiles/hiking/{z}/{x}/{y}.mvt`,
    });
    return new VectorTileLayer({
      zIndex: OldLayers.zIndexNetworkLayer,
      source: source,
      renderMode: 'vector',
      style: this.styleFunction(),
    });
  }

  private styleFunction(): StyleFunction {
    return (feature) => {
      // const routeId = feature.get('routeId');
      // const name = feature.get('name');
      // const layer = feature.get('layer');
      // const surface = feature.get('surface');
      // const segmentId = feature.get('segmentId');
      // const segmentElementId = feature.get('segmentElementId');
      // const pathIds = feature.get('pathIds');
      const scope = feature.get('layer');

      // console.log(
      //   `feature routeId=${routeId}, name=${name}, layer=${layer}, surface=${surface}, segmentId=${segmentId}, segmentElementId=${segmentElementId}, pathIds=${pathIds}`
      // );

      return ExploreStyle.style(scope, this.zoom);
    };
  }
}
