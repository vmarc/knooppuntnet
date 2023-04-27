// this file is generated, please do not modify

import { Country } from '@api/custom';
import { NetworkType } from '@api/custom';
import { ChangeSetElementRefs } from './change-set-element-refs';

export interface ChangeSetNetwork {
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
  readonly happy: boolean;
  readonly investigate: boolean;
}
