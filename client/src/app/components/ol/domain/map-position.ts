import { Params } from '@angular/router';
import { Coordinate } from 'ol/coordinate';
import { toLonLat } from 'ol/proj';
import { fromLonLat } from 'ol/proj';
import { NetworkMapPosition } from './network-map-position';

export class MapPosition {
  constructor(
    readonly zoom: number,
    readonly x: number,
    readonly y: number,
    readonly rotation: number
  ) {}

  static fromJSON(jsonObject: any): MapPosition {
    if (!jsonObject) {
      return undefined;
    }
    return new MapPosition(
      jsonObject.zoom,
      jsonObject.x,
      jsonObject.y,
      jsonObject.rotation
    );
  }

  static fromQueryParam(queryParam: string): MapPosition | null {
    if (!queryParam || queryParam.length === 0) {
      return null;
    }

    const parts = queryParam.split(',');
    if (parts.length !== 3) {
      return null;
    }

    const lat = +parts[0];
    const lon = +parts[1];
    const zoom = +parts[2];

    if (isNaN(lat) || isNaN(lon) || isNaN(zoom)) {
      return null;
    }

    const coordinate = fromLonLat([lon, lat]);
    return new MapPosition(zoom, coordinate[0], coordinate[1], 0);
  }

  toQueryParams(): Params {
    const position = this.toQueryParam();
    return { position };
  }

  toQueryParam(): string {
    const center: Coordinate = [this.x, this.y];
    const zoom = this.zoom;

    const c = toLonLat(center);
    const lng = c[0].toFixed(8);
    const lat = c[1].toFixed(8);
    const z = Math.round(zoom);

    return `${lat},${lng},${z}`;
  }

  toNetworkMapPosition(
    mapPosition: MapPosition,
    networkId: number
  ): NetworkMapPosition {
    return {
      networkId,
      zoom: mapPosition.zoom,
      x: mapPosition.x,
      y: mapPosition.y,
      rotation: mapPosition.rotation,
    };
  }
}
