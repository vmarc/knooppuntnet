// this file is generated, please do not modify

import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { Ref } from './ref';

export interface NetworkRefs {
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkRef: Ref;
  readonly refType: string;
  readonly refs: Ref[];
}
