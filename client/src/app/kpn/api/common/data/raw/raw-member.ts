// this class is generated, please do not modify

export class RawMember {

  constructor(readonly memberType: string,
              readonly ref: number,
              readonly role: string) {
  }

  public static fromJSON(jsonObject): RawMember {
    if (!jsonObject) {
      return undefined;
    }
    return new RawMember(
      jsonObject.memberType,
      jsonObject.ref,
      jsonObject.role
    );
  }
}
