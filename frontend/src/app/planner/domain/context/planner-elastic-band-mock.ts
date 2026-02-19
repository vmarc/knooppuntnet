import { Position } from 'geojson';
import { expect } from 'vitest';
import { expectCoordinate } from '../../util/test-support';
import { PlannerElasticBand } from './planner-elastic-band';

export class PlannerElasticBandMock implements PlannerElasticBand {
  private _anchor1: Position;
  private _anchor2: Position;
  private _position: Position;
  private _visible: boolean;

  // interface implementation

  set(anchor1: Position, anchor2: Position, position: Position) {
    this._anchor1 = anchor1;
    this._anchor2 = anchor2;
    this._position = position;
    this._visible = true;
  }

  setInvisible(): void {
    this._visible = false;
  }

  updatePosition(position: Position): void {
    this._position = position;
  }

  // assertions

  expectAnchor1(anchor: Position): void {
    expectCoordinate(this._anchor1, anchor);
  }

  expectAnchor2(anchor: Position): void {
    expectCoordinate(this._anchor2, anchor);
  }

  expectPosition(position: Position): void {
    expectCoordinate(this._position, position);
  }

  expectVisible(visible: boolean): void {
    expect(this._visible).toEqual(visible);
  }
}
