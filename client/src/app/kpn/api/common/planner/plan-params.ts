// this class is generated, please do not modify

export class PlanParams {
  constructor(readonly networkType: string, readonly planString: string) {}

  static fromJSON(jsonObject: any): PlanParams {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanParams(jsonObject.networkType, jsonObject.planString);
  }
}
