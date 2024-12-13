import { Country } from './country';
import { NetworkType } from '@api/common';

export interface Subset {
  readonly country: Country;
  readonly networkType: NetworkType;
}
