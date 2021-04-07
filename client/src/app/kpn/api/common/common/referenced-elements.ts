// this class is generated, please do not modify

export class ReferencedElements {
  constructor(
    readonly nodeIds: Array<number>,
    readonly routeIds: Array<number>
  ) {}

  static fromJSON(jsonObject: any): ReferencedElements {
    if (!jsonObject) {
      return undefined;
    }
    return new ReferencedElements(jsonObject.nodeIds, jsonObject.routeIds);
  }
}
