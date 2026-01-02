// this file is generated, please do not modify

import { GeometryDiffInfo } from './geometry-diff-info';
import { WayGeometry } from './way-geometry';
import { WayGeometryUpdate } from './way-geometry-update';

export interface GeometryDiff {
  readonly info: GeometryDiffInfo;
  readonly common: WayGeometry[];
  readonly update: WayGeometryUpdate[];
}
