// this file is generated, please do not modify

import { ChangeSetElementRefs } from './change-set-element-refs';
import { NetworkType } from '../custom/network-type';

export interface LocationTreeItem {
  readonly level: number;
  readonly locationName: string;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly networkType: NetworkType;
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
  readonly expandable: boolean;
  readonly children: LocationTreeItem[];
}
