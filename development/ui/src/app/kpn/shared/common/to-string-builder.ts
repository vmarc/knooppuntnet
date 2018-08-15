// this class is generated, please do not modify

export class ToStringBuilder {

  constructor(public className?: string,
              public strings?: Array<string>) {
  }

  public static fromJSON(jsonObject): ToStringBuilder {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ToStringBuilder();
    instance.className = jsonObject.className;
    instance.strings = jsonObject.strings;
    return instance;
  }
}

