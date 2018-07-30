// this class is generated, please do not modify

export class Timestamp {

  constructor(public year?: number,
              public month?: number,
              public day?: number,
              public hour?: number,
              public minute?: number,
              public second?: number) {
  }

  public static fromJSON(jsonObject): Timestamp {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Timestamp();
    instance.year = jsonObject.year;
    instance.month = jsonObject.month;
    instance.day = jsonObject.day;
    instance.hour = jsonObject.hour;
    instance.minute = jsonObject.minute;
    instance.second = jsonObject.second;
    return instance;
  }
}

