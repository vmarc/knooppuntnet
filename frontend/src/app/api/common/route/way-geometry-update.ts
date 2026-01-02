// this file is generated, please do not modify

import { WayLine } from './way-line';

export interface WayGeometryUpdate {
  readonly wayId: number;
  readonly common?: WayLine[];
  readonly added?: WayLine[];
  readonly removed?: WayLine[];
}
