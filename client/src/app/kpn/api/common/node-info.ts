// this file is generated, please do not modify

import { Country } from '../custom/country';
import { Day } from '../custom/day';
import { Fact } from '../custom/fact';
import { NodeIntegrity } from './node/node-integrity';
import { NodeName } from './node-name';
import { Reference } from './common/reference';
import { Tags } from '../custom/tags';
import { Timestamp } from '../custom/timestamp';

export interface NodeInfo {
  readonly _id: number;
  readonly id: number;
  readonly labels: string[];
  readonly active: boolean;
  readonly orphan: boolean;
  readonly country: Country;
  readonly name: string;
  readonly names: NodeName[];
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly tags: Tags;
  readonly facts: Fact[];
  readonly locations: string[];
  readonly tiles: string[];
  readonly integrity: NodeIntegrity;
  readonly routeReferences: Reference[];
}
