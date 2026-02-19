// this file is generated, please do not modify

import { ChangeKey } from '@api/common/changes/details/change-key';
import { LocationChangesInfo } from './location-changes-info';

export interface LocationChangeSetInfo {
  readonly rowIndex: number;
  readonly key: ChangeKey;
  readonly comment?: string;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly locationChanges: ReadonlyArray<LocationChangesInfo>;
}
