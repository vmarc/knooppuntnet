import {List} from "immutable";
import Coordinate from "ol/coordinate";

export class TestSupport {

  static expectCoordinate(actual: Coordinate, expected: Coordinate): void {
    expect(JSON.stringify(actual)).toEqual(JSON.stringify(expected));
  }

  static expectCoordinates(actual: List<Coordinate>, ...expected: Array<Coordinate>): void {
    expect(JSON.stringify(actual)).toEqual(JSON.stringify(expected));
  }

}
