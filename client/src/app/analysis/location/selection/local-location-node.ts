export interface LocalLocationNode {
  readonly path: string;
  readonly name: string;
  readonly nodeCount: number;
  readonly children: LocalLocationNode[];
}
