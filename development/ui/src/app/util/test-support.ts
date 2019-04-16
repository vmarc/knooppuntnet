import Coordinate from "ol/coordinate";

export class TestSupport {

  static expectCoordinate(actual: Coordinate, expected: Coordinate): void {
    expect(actual[0]).toBeCloseTo(expected[0], 0.01);
    expect(actual[1]).toBeCloseTo(expected[1], 0.01);
  }

}
