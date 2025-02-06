export interface LocalLocationNode {
  readonly path: string;
  readonly name: string;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly factCount: number;
  readonly children: LocalLocationNode[];
}
