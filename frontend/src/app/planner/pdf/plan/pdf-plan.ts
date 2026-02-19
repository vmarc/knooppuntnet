import { PdfPlanNode } from './pdf-plan-node';

export class PdfPlan {
  constructor(readonly nodes: ReadonlyArray<PdfPlanNode>) {}
}
