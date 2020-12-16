// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../../../custom/fact';
import {Ref} from '../../common/ref';
import {RawWay} from '../../data/raw/raw-way';
import {RouteData} from '../../diff/route-data';
import {RouteDiff} from '../../diff/route/route-diff';
import {WayUpdate} from '../../diff/way-update';
import {RouteLocationAnalysis} from '../../route-location-analysis';
import {ChangeKey} from './change-key';
import {ChangeType} from './change-type';

export class RouteChange {

  constructor(readonly key: ChangeKey,
              readonly changeType: ChangeType,
              readonly name: string,
              readonly locationAnalysis: RouteLocationAnalysis,
              readonly addedToNetwork: List<Ref>,
              readonly removedFromNetwork: List<Ref>,
              readonly before: RouteData,
              readonly after: RouteData,
              readonly removedWays: List<RawWay>,
              readonly addedWays: List<RawWay>,
              readonly updatedWays: List<WayUpdate>,
              readonly diffs: RouteDiff,
              readonly facts: List<Fact>,
              readonly happy: boolean,
              readonly investigate: boolean,
              readonly locationHappy: boolean,
              readonly locationInvestigate: boolean) {
  }

  public static fromJSON(jsonObject: any): RouteChange {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChange(
      ChangeKey.fromJSON(jsonObject.key),
      ChangeType.fromJSON(jsonObject.changeType),
      jsonObject.name,
      RouteLocationAnalysis.fromJSON(jsonObject.locationAnalysis),
      jsonObject.addedToNetwork ? List(jsonObject.addedToNetwork.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.removedFromNetwork ? List(jsonObject.removedFromNetwork.map((json: any) => Ref.fromJSON(json))) : List(),
      RouteData.fromJSON(jsonObject.before),
      RouteData.fromJSON(jsonObject.after),
      jsonObject.removedWays ? List(jsonObject.removedWays.map((json: any) => RawWay.fromJSON(json))) : List(),
      jsonObject.addedWays ? List(jsonObject.addedWays.map((json: any) => RawWay.fromJSON(json))) : List(),
      jsonObject.updatedWays ? List(jsonObject.updatedWays.map((json: any) => WayUpdate.fromJSON(json))) : List(),
      RouteDiff.fromJSON(jsonObject.diffs),
      jsonObject.facts ? List(jsonObject.facts.map((json: any) => Fact.fromJSON(json))) : List(),
      jsonObject.happy,
      jsonObject.investigate,
      jsonObject.locationHappy,
      jsonObject.locationInvestigate
    );
  }
}
