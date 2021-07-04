// this class is generated, please do not modify

import { List } from 'immutable';
import { Day } from '@api/custom/day';
import { Fact } from '@api/custom/fact';
import { Ref } from '../common/ref';
import { Tags } from '@api/custom/tags';
import { Timestamp } from '@api/custom/timestamp';

export class NetworkNodeDetail {
  constructor(
    readonly id: number,
    readonly name: string,
    readonly number: string,
    readonly latitude: string,
    readonly longitude: string,
    readonly connection: boolean,
    readonly roleConnection: boolean,
    readonly definedInRelation: boolean,
    readonly definedInRoute: boolean,
    readonly proposed: boolean,
    readonly timestamp: Timestamp,
    readonly lastSurvey: Day,
    readonly expectedRouteCount: string,
    readonly routeReferences: List<Ref>,
    readonly facts: List<Fact>,
    readonly tags: Tags
  ) {}

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
      jsonObject.proposed,
      Timestamp.fromJSON(jsonObject.timestamp),
      Day.fromJSON(jsonObject.lastSurvey),
      jsonObject.expectedRouteCount,
      jsonObject.routeReferences
        ? List(
            jsonObject.routeReferences.map((json: any) => Ref.fromJSON(json))
          )
        : List(),
      jsonObject.facts
        ? List(jsonObject.facts.map((json: any) => Fact.fromJSON(json)))
        : List(),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
