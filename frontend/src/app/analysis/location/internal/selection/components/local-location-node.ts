import { NzTreeNodeOptions } from 'ng-zorro-antd/tree';

export interface LocalLocationNode extends NzTreeNodeOptions {
  readonly path: string;
  readonly name: string;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly factCount: number;
  readonly children: LocalLocationNode[];
}
