import { Country } from './country';
import { NetworkType } from './network-type';

export interface Subset {
  readonly country: Country;
  readonly networkType: NetworkType;
}
