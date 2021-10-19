// this file is generated, please do not modify

import { Country } from '../custom/country';
import { NetworkScope } from '../custom/network-scope';
import { NetworkType } from '../custom/network-type';
import { Tags } from '../custom/tags';
import { Timestamp } from '../custom/timestamp';

export interface RouteSummary {
  readonly id: number;
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly name: string;
  readonly meters: number;
  readonly broken: boolean;
  readonly inaccessible: boolean;
  readonly wayCount: number;
  readonly timestamp: Timestamp;
  readonly nodeNames: string[];
  readonly tags: Tags;
}
