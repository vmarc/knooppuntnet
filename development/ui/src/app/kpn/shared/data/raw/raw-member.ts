// this class is generated, please do not modify

export class RawMember {

  constructor(public memberType?: string,
              public ref?: number,
              public role?: string) {
  }

  public static fromJSON(jsonObject): RawMember {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RawMember();
    instance.memberType = jsonObject.memberType;
    instance.ref = jsonObject.ref;
    instance.role = jsonObject.role;
    return instance;
  }
}

