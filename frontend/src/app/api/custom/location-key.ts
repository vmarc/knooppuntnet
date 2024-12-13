import { Country } from './country';
import { NetworkType } from '@api/common';

export interface LocationKey {
  readonly networkType: NetworkType;
  readonly country: Country;
  readonly name: string;
}
