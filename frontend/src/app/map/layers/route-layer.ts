import { effect } from '@angular/core';
import { Signal } from '@angular/core';
import { NetworkType } from '@api/custom';
import { ZoomLevel } from '@app/ol/domain';
import { OldLayers } from '@app/ol/layers';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { ExploreStyle } from '../style/explore-style';

export class RouteLayer {
  private zoom: number; // local copy for performance reasons

  constructor(zoomSignal: Signal<number>) {
    effect(() => {
      this.zoom = zoomSignal();
    });
  }

  build(networkType: NetworkType): VectorTileLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: ZoomLevel.newMinZoom,
      maxZoom: ZoomLevel.newMaxZoom,
      format: new MVT(),
      url: `/tiles/${networkType}/{z}/{x}/{y}.mvt`,
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
      const layer = feature.get('layer');
      const scope = feature.get('scope');
      return ExploreStyle.style(layer, scope, this.zoom);
    };
  }
}
