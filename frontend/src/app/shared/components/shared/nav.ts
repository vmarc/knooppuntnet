import { ParamMap } from '@angular/router';

export class Nav {
  constructor(
    private readonly paramMap: ParamMap,
    private readonly queryParamMap: ParamMap,
    private readonly navigationState?: {
      [k: string]: any;
    }
  ) {}

  public param(name: string): string {
    return this.paramMap.get(name);
  }

  public queryParam(name: string): string {
    return this.queryParamMap.get(name);
  }

  public state(name: string): string {
    return this.navigationState && this.navigationState[name]
      ? this.navigationState[name]
      : '';
  }
}
