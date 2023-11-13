// this file is generated, please do not modify

import { Country } from '@api/custom';
import { NetworkScope } from '@api/custom';
import { NetworkType } from '@api/custom';
import { Tags } from '@api/custom';
import { Timestamp } from '@api/custom';

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
