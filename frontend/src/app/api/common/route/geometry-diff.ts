// this file is generated, please do not modify

import { WayGeometry } from './way-geometry';
import { WayGeometryUpdate } from './way-geometry-update';

export interface GeometryDiff {
  readonly common: WayGeometry[];
  readonly update: WayGeometryUpdate[];
}
