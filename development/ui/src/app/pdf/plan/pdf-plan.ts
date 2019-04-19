import {List} from "immutable";
import {PdfPlanNode} from "./pdf-plan-node";

export class PdfPlan {

  constructor(readonly nodes: List<PdfPlanNode>) {
  }

}
