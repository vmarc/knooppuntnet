import {NodeName} from './node-name';
import {Tag} from './tag';
import {Country} from "./country";

export interface NodeInfo {
  readonly id: number;
  readonly active: boolean;
  readonly orphan: boolean;
  readonly country: Country;
  readonly name: string;
  readonly names: NodeName[];
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: string;
  readonly lastSurvey: string;
  readonly tags: Tag[];
  readonly facts: string[];
  readonly location: Location;
  readonly tiles: string[];
}
