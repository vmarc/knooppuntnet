import { Country } from "@api/common";
import { NetworkType } from '@api/common';

export interface Subset {
  readonly country: Country;
  readonly networkType: NetworkType;
}
