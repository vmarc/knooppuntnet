import Coordinate from 'ol/coordinate';
import {PlannerElasticBand} from "./planner-elastic-band";

export class PlannerElasticBandMock implements PlannerElasticBand {

  private _anchor1: Coordinate;
  private _anchor2: Coordinate;
  private _position: Coordinate;
  private _visible: boolean;

  set(anchor1: Coordinate, anchor2: Coordinate, position: Coordinate) {
    this._anchor1 = anchor1;
    this._anchor2 = anchor2;
    this._position = position;
    this._visible = true;
  }

  setInvisible(): void {
    this._visible = false;
  }

  updatePosition(position: Coordinate) {
    this._position = position;
  }

  anchor1(): Coordinate {
    return this._anchor1;
  }

  anchor2(): Coordinate {
    return this._anchor2;
  }

  position(): Coordinate {
    return this._position;
  }

  isVisible(): boolean {
    return this._visible;
  }

}
