import Coordinate from "ol/coordinate";
import {PlannerElasticBand} from "./planner-elastic-band";

export class PlannerElasticBandMock implements PlannerElasticBand {

  private _anchor1: Coordinate;
  private _anchor2: Coordinate;
  private _position: Coordinate;
  private _visible: boolean;

  // interface implementation

  set(anchor1: Coordinate, anchor2: Coordinate, position: Coordinate) {
    this._anchor1 = anchor1;
    this._anchor2 = anchor2;
    this._position = position;
    this._visible = true;
  }

  setInvisible(): void {
    this._visible = false;
  }

  updatePosition(position: Coordinate): void {
    this._position = position;
  }

  // assertions

  expectAnchor1(anchor: Coordinate): void {
    expect(this._anchor1).toEqual(anchor);
  }

  expectAnchor2(anchor: Coordinate): void {
    expect(this._anchor2).toEqual(anchor);
  }

  expectPosition(position: Coordinate): void {
    expect(this._position).toEqual(position);
  }

  expectVisible(visible: boolean): void {
    expect(this._visible).toEqual(visible);
  }

}
