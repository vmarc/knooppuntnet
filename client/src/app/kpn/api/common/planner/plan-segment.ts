// this class is generated, please do not modify

import {PlanFragment} from './plan-fragment';

export class PlanSegment {

  constructor(readonly meters: number,
              readonly surface: string,
              readonly colour: string,
              readonly fragments: Array<PlanFragment>) {
  }

  public static fromJSON(jsonObject: any): PlanSegment {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanSegment(
      jsonObject.meters,
      jsonObject.surface,
      jsonObject.colour,
      jsonObject.fragments.map((json: any) => PlanFragment.fromJSON(json))
    );
  }
}
