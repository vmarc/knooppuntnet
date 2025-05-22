// this file is generated, please do not modify

import { NodeIntegrity } from '@api/common/node/node-integrity';
import { Day } from '@api/custom/day';
import { Tag } from '@api/custom/tag';
import { Timestamp } from '@api/custom/timestamp';
import { Country } from './country';
import { Fact } from './fact';
import { LocationInfo } from './location-info';
import { NodeName } from './node-name';

export interface NodeInfo {
  readonly id: number;
  readonly active: boolean;
  readonly orphan: boolean;
  readonly country?: Country;
  readonly name: string;
  readonly names: NodeName[];
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey?: Day;
  readonly tags: Tag[];
  readonly facts: Fact[];
  readonly locations: LocationInfo[];
  readonly tiles: string[];
  readonly integrity?: NodeIntegrity;
}
