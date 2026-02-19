// this file is generated, please do not modify

import { WayLine } from './way-line';

export interface WayGeometryUpdate {
  readonly wayId: number;
  readonly common?: ReadonlyArray<WayLine>;
  readonly added?: ReadonlyArray<WayLine>;
  readonly removed?: ReadonlyArray<WayLine>;
}
