import {List} from "immutable";
import {PdfPlanNode} from "./pdf-plan-node";

export class PdfPlan {

  constructor(public readonly nodes: List<PdfPlanNode>) {
  }

}
