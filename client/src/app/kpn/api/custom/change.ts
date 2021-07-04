import { List } from 'immutable';

export class Change {
  readonly action: number;
  readonly elements: List<any>;

  constructor(action: number, elements: List<any>) {
    this.action = action;
    this.elements = elements;
  }

  public static fromJSON(jsonObject: any): Change {
    if (!jsonObject) {
      return undefined;
    }
    return new Change(
      jsonObject.action,
      jsonObject.elements ? List(jsonObject.elements) : List()
    );
  }
}
