import { Coordinate } from '@api/custom/coordinate';
import { LongCoordinate } from '@app/ol/layers/long-coordinate';
import { StringCoordinate } from '@app/ol/layers/string-coordinate';

export class CoordinateCodec {
  static readonly CoordinatePrecision = 10000000; // 7 decimal places precision

  static decode(coordinateString: string): Coordinate[] {
    const array: StringCoordinate[] = JSON.parse(coordinateString);
    if (array.length == 0) {
      return [];
    }
    if (array.length == 1) {
      return [[parseFloat(array[0][0]), parseFloat(array[0][1])]];
    }

    const headCoordinate: Coordinate = [parseFloat(array[0][0]), parseFloat(array[0][1])];
    const headLongCoordinate = this.toLongCoordinate(array[0]);
    const tailLongCoordinates = array.slice(1).map((c) => this.stringCoordinateToLongCoordinate(c));
    const longCoordinates: LongCoordinate[] = [headLongCoordinate, ...tailLongCoordinates];
    const decoded = this.deltaDecode(longCoordinates);
    return [headCoordinate, ...decoded.slice(0).map((c) => this.toCoordinate(c))];
  }

  private static deltaDecode(coordinates: LongCoordinate[]): LongCoordinate[] {
    if (coordinates.length === 0) {
      return [];
    }
    return coordinates.slice(1).reduce(
      (acc: LongCoordinate[], delta: LongCoordinate) => {
        const prev = acc[acc.length - 1];
        acc.push(new LongCoordinate(prev.x + delta.x, prev.y + delta.y));
        return acc;
      },
      [coordinates[0]]
    );
  }

  private static toLongCoordinate(coordinate: StringCoordinate): LongCoordinate {
    const x = parseFloat(coordinate[0]);
    const y = parseFloat(coordinate[1]);
    const xLong = Math.round(x * this.CoordinatePrecision);
    const yLong = Math.round(y * this.CoordinatePrecision);
    return new LongCoordinate(xLong, yLong);
  }

  private static toCoordinate(coordinate: LongCoordinate): Coordinate {
    const x = coordinate.x / this.CoordinatePrecision;
    const y = coordinate.y / this.CoordinatePrecision;
    return [x, y];
  }

  private static stringCoordinateToLongCoordinate(coordinate: StringCoordinate): LongCoordinate {
    return new LongCoordinate(parseFloat(coordinate[0]), parseFloat(coordinate[1]));
  }
}
