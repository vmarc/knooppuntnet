// this file is generated, please do not modify

import { WayInfo } from './way-info';
import { WayUpdate } from './way-update';

export interface WayDiffsInfo {
  readonly removed: WayInfo[];
  readonly added: WayInfo[];
  readonly updated: WayUpdate[];
}
