// this file is generated, please do not modify

import { NetworkType } from '@api/custom';
import { ChangeSetElementRefs } from './change-set-element-refs';

export interface LocationChanges {
  readonly networkType: NetworkType;
  readonly locationNames: string[];
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
  readonly happy: boolean;
  readonly investigate: boolean;
}
