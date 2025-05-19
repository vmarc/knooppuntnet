// this file is generated, please do not modify

import { RawWay } from '@api/common/data/raw/raw-way';
import { WayUpdate } from './way-update';

export interface WayDiffs {
  readonly removed: RawWay[];
  readonly added: RawWay[];
  readonly updated: WayUpdate[];
}
