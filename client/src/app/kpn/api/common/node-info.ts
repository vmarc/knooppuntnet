// this class is generated, please do not modify

import { Country } from '../custom/country';
import { Day } from '../custom/day';
import { Fact } from '../custom/fact';
import { Location } from './location/location';
import { NodeName } from './node-name';
import { Tags } from '../custom/tags';
import { Timestamp } from '../custom/timestamp';

export class NodeInfo {
  constructor(
    readonly id: number,
    readonly active: boolean,
    readonly orphan: boolean,
    readonly country: Country,
    readonly name: string,
    readonly names: Array<NodeName>,
    readonly latitude: string,
    readonly longitude: string,
    readonly lastUpdated: Timestamp,
    readonly lastSurvey: Day,
    readonly tags: Tags,
    readonly facts: Array<Fact>,
    readonly location: Location,
    readonly tiles: Array<string>
  ) {}

  static fromJSON(jsonObject: any): NodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeInfo(
      jsonObject.id,
      jsonObject.active,
      jsonObject.orphan,
      jsonObject.country,
      jsonObject.name,
      jsonObject.names.map((json: any) => NodeName.fromJSON(json)),
      jsonObject.latitude,
      jsonObject.longitude,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      Day.fromJSON(jsonObject.lastSurvey),
      Tags.fromJSON(jsonObject.tags),
      jsonObject.facts,
      Location.fromJSON(jsonObject.location),
      jsonObject.tiles
    );
  }
}
