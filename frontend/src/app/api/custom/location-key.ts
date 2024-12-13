import { Country } from "@api/common";
import { NetworkType } from '@api/common';

export interface LocationKey {
  readonly networkType: NetworkType;
  readonly country: Country;
  readonly name: string;
}
