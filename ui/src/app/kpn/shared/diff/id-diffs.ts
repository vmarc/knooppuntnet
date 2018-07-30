// this class is generated, please do not modify

export class IdDiffs {

  constructor(public removed?: Array<number>,
              public added?: Array<number>,
              public updated?: Array<number>) {
  }

  public static fromJSON(jsonObject): IdDiffs {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new IdDiffs();
    instance.removed = jsonObject.removed;
    instance.added = jsonObject.added;
    instance.updated = jsonObject.updated;
    return instance;
  }
}

