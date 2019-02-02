// this class is generated, please do not modify

import {List} from 'immutable';

export class ToStringBuilder {

  constructor(readonly className: string,
              readonly strings: List<string>) {
  }

  public static fromJSON(jsonObject): ToStringBuilder {
    if (!jsonObject) {
      return undefined;
    }
    return new ToStringBuilder(
      jsonObject.className,
      jsonObject.strings ? List(jsonObject.strings) : List()
    );
  }
}
