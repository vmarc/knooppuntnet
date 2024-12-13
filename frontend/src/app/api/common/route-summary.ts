// this file is generated, please do not modify

import { Country } from '@api/custom';
import { Tag } from '@api/custom';
import { Timestamp } from '@api/custom';
import { NetworkType } from './network-type';

export interface RouteSummary {
  readonly id: number;
  readonly countries: Country[];
  readonly nodeNetwork: boolean;
  readonly networkTypes: NetworkType[];
  readonly scopes: string[];
  readonly name: string;
  readonly meters: number;
  readonly broken: boolean;
  readonly inaccessible: boolean;
  readonly wayCount: number;
  readonly timestamp: Timestamp;
  readonly tags: Tag[];
}
