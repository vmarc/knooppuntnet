import { Country } from './country';
import { NetworkType } from './network-type';

export interface LocationKey {
  readonly networkType: NetworkType;
  readonly country: Country;
  readonly name: string;
}
