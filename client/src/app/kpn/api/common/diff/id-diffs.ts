// this class is generated, please do not modify

export class IdDiffs {
  constructor(
    readonly removed: Array<number>,
    readonly added: Array<number>,
    readonly updated: Array<number>
  ) {}

  static fromJSON(jsonObject: any): IdDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new IdDiffs(
      jsonObject.removed,
      jsonObject.added,
      jsonObject.updated
    );
  }
}
