// this class is generated, please do not modify

import {Subset} from '../custom/subset';

export class ChangeSetSubsetAnalysis {

  constructor(readonly subset: Subset,
              readonly happy: boolean,
              readonly investigate: boolean) {
  }

  public static fromJSON(jsonObject: any): ChangeSetSubsetAnalysis {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetSubsetAnalysis(
      Subset.fromJSON(jsonObject.subset),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
