// this file is generated, please do not modify

import { ChangeSetElementRefs } from './change-set-element-refs';
import { NetworkType } from '../custom/network-type';

export interface ChangeSetLocation {
  readonly networkType: NetworkType;
  readonly locationName: string;
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
  readonly happy: boolean;
  readonly investigate: boolean;
}
