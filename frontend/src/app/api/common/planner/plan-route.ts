// this file is generated, please do not modify

import { PlanNode } from './plan-node';
import { PlanSegment } from './plan-segment';

export interface PlanRoute {
  readonly sourceNode: PlanNode;
  readonly sinkNode: PlanNode;
  readonly meters: number;
  readonly segments: PlanSegment[];
  readonly streets: string[];
}
