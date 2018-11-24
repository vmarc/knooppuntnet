// this class is generated, please do not modify

export class RawMember {
  readonly memberType: string;
  readonly ref: number;
  readonly role: string;

  constructor(memberType: string,
              ref: number,
              role: string) {
    this.memberType = memberType;
    this.ref = ref;
    this.role = role;
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
