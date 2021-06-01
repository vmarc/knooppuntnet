// this file is generated, please do not modify

import { Country } from '../custom/country';
import { Day } from '../custom/day';
import { Fact } from '../custom/fact';
import { Location } from './location/location';
import { NodeName } from './node-name';
import { Tags } from '../custom/tags';
import { Timestamp } from '../custom/timestamp';

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
  readonly tags: Tags;
  readonly facts: Fact[];
  readonly location: Location;
  readonly tiles: string[];
}
