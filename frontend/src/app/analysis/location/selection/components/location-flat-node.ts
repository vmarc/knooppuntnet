export class LocationFlatNode {
  constructor(
    public expandable: boolean,
    public path: string,
    public name: string,
    public nodeCount: number,
    public routeCount: number,
    public factCount: number,
    public level: number
  ) {}

  isUsed(): boolean {
    return this.nodeCount > 0 || this.routeCount > 0 || this.factCount > 0;
  }
}
