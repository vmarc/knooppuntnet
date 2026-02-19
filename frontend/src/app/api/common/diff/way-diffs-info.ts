// this file is generated, please do not modify

import { WayInfo } from './way-info';
import { WayUpdate } from './way-update';

export interface WayDiffsInfo {
  readonly removed: ReadonlyArray<WayInfo>;
  readonly added: ReadonlyArray<WayInfo>;
  readonly updated: ReadonlyArray<WayUpdate>;
}
