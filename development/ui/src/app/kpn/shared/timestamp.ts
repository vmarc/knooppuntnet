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
    instance.year = +jsonObject.substr(0, 4);
    instance.month = +jsonObject.substr(5, 2);
    instance.day = +jsonObject.substr(8, 2);
    instance.hour = +jsonObject.substr(11, 2);
    instance.minute = +jsonObject.substr(14, 2);
    instance.second = +jsonObject.substr(17, 2);
    return instance;
  }
}

