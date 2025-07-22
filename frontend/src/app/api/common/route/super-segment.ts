// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { SuperSubSegment } from './super-sub-segment';

export interface SuperSegment {
  readonly id: number;
  readonly bounds?: Bounds;
  readonly segments: SuperSubSegment[];
}
