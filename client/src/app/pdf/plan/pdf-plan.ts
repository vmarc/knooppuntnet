import { PdfPlanNode } from '@app/pdf/plan/pdf-plan-node';
import { List } from 'immutable';

export class PdfPlan {
  constructor(readonly nodes: List<PdfPlanNode>) {}
}
