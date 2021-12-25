// this file is generated, please do not modify

import { ChangeSetElementRefs } from './change-set-element-refs';
import { NetworkType } from '../custom/network-type';

export interface LocationChanges {
  readonly networkType: NetworkType;
  readonly locationNames: string[];
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
}
