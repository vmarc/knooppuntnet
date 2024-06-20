// this file is generated, please do not modify

import { Reference } from '@api/common/common';
import { NodeIntegrity } from '@api/common/node';
import { Country } from '@api/custom';
import { Day } from '@api/custom';
import { Fact } from '@api/custom';
import { Tag } from '@api/custom';
import { Timestamp } from '@api/custom';
import { LocationInfo } from './location-info';
import { NodeName } from './node-name';

export interface NodeInfo {
  readonly id: number;
  readonly active: boolean;
  readonly orphan: boolean;
  readonly country: Country;
  readonly name: string;
  readonly names: NodeName[];
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly tags: Tag[];
  readonly facts: Fact[];
  readonly locations: LocationInfo[];
  readonly tiles: string[];
  readonly integrity: NodeIntegrity;
  readonly routeReferences: Reference[];
}
