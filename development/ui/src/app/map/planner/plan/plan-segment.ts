import {List} from "immutable";
import {PlanFragment} from "./plan-fragment";

export class PlanSegment {

  constructor(readonly meters: number,
              readonly surface: string,
              readonly fragments: List<PlanFragment>) {
  }
}
