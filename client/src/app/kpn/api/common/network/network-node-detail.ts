// this class is generated, please do not modify

import {Day} from '../../custom/day';
import {Fact} from '../../custom/fact';
import {Tags} from '../../custom/tags';
import {Timestamp} from '../../custom/timestamp';
import {Ref} from '../common/ref';

export class NetworkNodeDetail {

  constructor(readonly id: number,
              readonly name: string,
              readonly number: string,
              readonly latitude: string,
              readonly longitude: string,
              readonly connection: boolean,
              readonly roleConnection: boolean,
              readonly definedInRelation: boolean,
              readonly definedInRoute: boolean,
              readonly timestamp: Timestamp,
              readonly lastSurvey: Day,
              readonly expectedRouteCount: string,
              readonly routeReferences: Array<Ref>,
              readonly facts: Array<Fact>,
              readonly tags: Tags) {
  }

  public static fromJSON(jsonObject: any): NetworkNodeDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodeDetail(
      jsonObject.id,
      jsonObject.name,
      jsonObject.number,
      jsonObject.latitude,
      jsonObject.longitude,
      jsonObject.connection,
      jsonObject.roleConnection,
      jsonObject.definedInRelation,
      jsonObject.definedInRoute,
      Timestamp.fromJSON(jsonObject.timestamp),
      Day.fromJSON(jsonObject.lastSurvey),
      jsonObject.expectedRouteCount,
      jsonObject.routeReferences.map((json: any) => Ref.fromJSON(json)),
      jsonObject.facts.map((json: any) => Fact.fromJSON(json)),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
