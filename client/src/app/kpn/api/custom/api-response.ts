import { Timestamp } from './timestamp';

export class ApiResponse<T> {
  constructor(
    public situationOn?: Timestamp,
    public version?: number,
    public result?: T
  ) {}

  public static fromJSON<T>(
    jsonObject: any,
    resultsFromJSON: (jsonObject2: any) => T
  ): ApiResponse<T> {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ApiResponse<T>();
    instance.situationOn = Timestamp.fromJSON(jsonObject.situationOn);
    instance.version = jsonObject.version;
    instance.result = resultsFromJSON(jsonObject.result);
    return instance;
  }
}
