import {List} from "immutable";
import Coordinate from "ol/coordinate";

export class TestSupport {

  static expectCoordinate(actual: Coordinate, expected: Coordinate): void {
    expect(actual[0]).toBeCloseTo(expected[0], 0.01);
    expect(actual[1]).toBeCloseTo(expected[1], 0.01);
  }

  static expectCoordinates(actual: List<Coordinate>, ...expected: Array<Coordinate>): void {
    expect(JSON.stringify(actual)).toEqual(JSON.stringify(expected));
  }

}
