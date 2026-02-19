// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { GeometryDiffInfo } from './geometry-diff-info';
import { WayGeometry } from './way-geometry';
import { WayGeometryUpdate } from './way-geometry-update';

export interface GeometryDiff {
  readonly info: GeometryDiffInfo;
  readonly common: ReadonlyArray<WayGeometry>;
  readonly update: ReadonlyArray<WayGeometryUpdate>;
  readonly bounds: Bounds;
}
