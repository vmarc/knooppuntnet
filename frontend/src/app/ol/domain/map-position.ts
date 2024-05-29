import { Coordinate } from 'ol/coordinate';
import { toLonLat } from 'ol/proj';
import { fromLonLat } from 'ol/proj';
import { CachedMapPosition } from './cached-map-position';

export class MapPosition {
  constructor(
    readonly zoom: number,
    readonly x: number,
    readonly y: number,
    readonly rotation: number
  ) {}

  static fromCachedMapPosition(cachedMapPosition: CachedMapPosition): MapPosition {
    if (!cachedMapPosition) {
      return undefined;
    }
    return new MapPosition(
      cachedMapPosition.zoom,
      cachedMapPosition.x,
      cachedMapPosition.y,
      cachedMapPosition.rotation
    );
  }

  static fromJSON(jsonObject: any): MapPosition {
    if (!jsonObject) {
      return undefined;
    }
    return new MapPosition(jsonObject.zoom, jsonObject.x, jsonObject.y, jsonObject.rotation);
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

  static toQueryParam(mapPosition: MapPosition): string {
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    const zoom = mapPosition.zoom;
    const c = toLonLat(center);
    const lng = c[0].toFixed(8);
    const lat = c[1].toFixed(8);
    const z = Math.round(zoom);
    return `${lat},${lng},${z}`;
  }

  toCachedMapPosition(id: string): CachedMapPosition {
    return {
      id: id,
      zoom: this.zoom,
      x: this.x,
      y: this.y,
      rotation: this.rotation,
    };
  }

  sameAs(other: MapPosition): boolean {
    return (
      this.zoom == other.zoom &&
      this.x == other.x &&
      this.y == other.y &&
      this.rotation == other.rotation
    );
  }
}
