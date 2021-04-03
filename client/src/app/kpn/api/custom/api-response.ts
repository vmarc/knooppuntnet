import {Timestamp} from './timestamp';

export class ApiResponse<T> {

  constructor(public situationOn?: Timestamp,
              public version?: number,
              public result?: T) {
  }

  public static fromJSON<R>(jsonObject: any, resultsFromJSON: (jsonObject2: any) => R): ApiResponse<R> {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ApiResponse<R>();
    instance.situationOn = Timestamp.fromJSON(jsonObject.situationOn);
    instance.version = jsonObject.version;
    instance.result = resultsFromJSON(jsonObject.result);
    return instance;
  }
}
