// this class is generated, please do not modify

import {List} from "immutable";
import {ChangeKey} from "./change-key";
import {ChangeType} from "./change-type";
import {Fact} from "../../fact";
import {RawWay} from "../../data/raw/raw-way";
import {Ref} from "../../common/ref";
import {RouteData} from "../../diff/route-data";
import {RouteDiff} from "../../diff/route/route-diff";
import {WayUpdate} from "../../diff/way-update";

export class RouteChange {

  constructor(readonly key: ChangeKey,
              readonly changeType: ChangeType,
              readonly name: string,
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
              readonly investigate: boolean) {
  }

  public static fromJSON(jsonObject): RouteChange {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChange(
      ChangeKey.fromJSON(jsonObject.key),
      ChangeType.fromJSON(jsonObject.changeType),
      jsonObject.name,
      jsonObject.addedToNetwork ? List(jsonObject.addedToNetwork.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.removedFromNetwork ? List(jsonObject.removedFromNetwork.map(json => Ref.fromJSON(json))) : List(),
      RouteData.fromJSON(jsonObject.before),
      RouteData.fromJSON(jsonObject.after),
      jsonObject.removedWays ? List(jsonObject.removedWays.map(json => RawWay.fromJSON(json))) : List(),
      jsonObject.addedWays ? List(jsonObject.addedWays.map(json => RawWay.fromJSON(json))) : List(),
      jsonObject.updatedWays ? List(jsonObject.updatedWays.map(json => WayUpdate.fromJSON(json))) : List(),
      RouteDiff.fromJSON(jsonObject.diffs),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List(),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
