// this class is generated, please do not modify

import { ChangeKey } from './changes/details/change-key';
import { ChangeSetSubsetAnalysis } from './change-set-subset-analysis';
import { ChangeSetSubsetElementRefs } from './change-set-subset-element-refs';
import { NetworkChanges } from './network-changes';
import { Subset } from '../custom/subset';
import { Timestamp } from '../custom/timestamp';

export class ChangeSetSummary {
  constructor(
    readonly key: ChangeKey,
    readonly subsets: Array<Subset>,
    readonly timestampFrom: Timestamp,
    readonly timestampUntil: Timestamp,
    readonly networkChanges: NetworkChanges,
    readonly orphanRouteChanges: Array<ChangeSetSubsetElementRefs>,
    readonly orphanNodeChanges: Array<ChangeSetSubsetElementRefs>,
    readonly subsetAnalyses: Array<ChangeSetSubsetAnalysis>,
    readonly happy: boolean,
    readonly investigate: boolean
  ) {}

  static fromJSON(jsonObject: any): ChangeSetSummary {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetSummary(
      ChangeKey.fromJSON(jsonObject.key),
      jsonObject.subsets?.map((json: any) => Subset.fromJSON(json)),
      Timestamp.fromJSON(jsonObject.timestampFrom),
      Timestamp.fromJSON(jsonObject.timestampUntil),
      NetworkChanges.fromJSON(jsonObject.networkChanges),
      jsonObject.orphanRouteChanges?.map((json: any) =>
        ChangeSetSubsetElementRefs.fromJSON(json)
      ),
      jsonObject.orphanNodeChanges?.map((json: any) =>
        ChangeSetSubsetElementRefs.fromJSON(json)
      ),
      jsonObject.subsetAnalyses?.map((json: any) =>
        ChangeSetSubsetAnalysis.fromJSON(json)
      ),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
