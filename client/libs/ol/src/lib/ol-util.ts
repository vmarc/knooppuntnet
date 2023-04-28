import { LatLonImpl } from '@api/common';
import { Coordinate } from 'ol/coordinate';
import { fromLonLat } from 'ol/proj';
import { toLonLat } from 'ol/proj';

export class OlUtil {
  public static latLonFromCoordinate(coordinate: Coordinate): LatLonImpl {
    const lonLat = toLonLat(coordinate);
    return {
      latitude: '' + lonLat[1],
      longitude: '' + lonLat[0],
    };
  }

  public static latLonToCoordinate(latLon: LatLonImpl): Coordinate {
    return this.toCoordinate(latLon.latitude, latLon.longitude);
  }

  public static toCoordinate(latitude: string, longitude: string): Coordinate {
    const latNumber = parseFloat(latitude);
    const lonNumber = parseFloat(longitude);
    return fromLonLat([lonNumber, latNumber]);
  }

  public static coordinateToString(coordinate: Coordinate): string {
    if (coordinate) {
      const lonLat = toLonLat(coordinate);
      return '[' + lonLat[1] + ', ' + lonLat[0] + ']';
    }
    return '[]';
  }
}
