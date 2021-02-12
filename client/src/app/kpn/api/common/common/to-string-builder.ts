// this class is generated, please do not modify

export class ToStringBuilder {

  constructor(readonly className: string,
              readonly strings: Array<string>) {
  }

  public static fromJSON(jsonObject: any): ToStringBuilder {
    if (!jsonObject) {
      return undefined;
    }
    return new ToStringBuilder(
      jsonObject.className,
      jsonObject.strings
    );
  }
}
