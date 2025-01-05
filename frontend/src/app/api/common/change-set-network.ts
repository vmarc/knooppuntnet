// this file is generated, please do not modify

import { ChangeSetElementRefs } from './change-set-element-refs';
import { Country } from './country';
import { NetworkType } from './network-type';

export interface ChangeSetNetwork {
  readonly country?: Country;
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
  readonly happy: boolean;
  readonly investigate: boolean;
}
