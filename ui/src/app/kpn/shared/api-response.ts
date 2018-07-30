import {Timestamp} from './timestamp';

export class ApiResponse<T> {

  constructor(public situationOn?: Timestamp,
              public version?: number,
              public result?: any) {
  }

  public static fromJSON<T>(jsonObject, converter: (jsonObject2) => T): ApiResponse<T> {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ApiResponse<T>();
    instance.situationOn = jsonObject.situationOn;
    instance.version = jsonObject.version;
    instance.result = converter(jsonObject.result);
    return instance;
  }
}

