import Coordinate from "ol/coordinate";
import {PlannerCrosshair} from "./planner-crosshair";

export class PlannerCrosshairMock implements PlannerCrosshair {

  private _visible: boolean = false;
  private _position: Coordinate = null;

  setVisible(visible: boolean) {
    this._visible = visible;
  }

  updatePosition(position: Coordinate) {
    this._position = position;
  }

  expectVisible(visible: boolean): void {
    expect(this._visible).toEqual(visible);
  }

  expectPosition(position: Coordinate): void {
    expect(this._position[0]).toBeCloseTo(position[0], 0.01);
    expect(this._position[1]).toBeCloseTo(position[1], 0.01);
  }

}
