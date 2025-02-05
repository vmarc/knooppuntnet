import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ParamMap } from '@angular/router';
import { Params } from '@angular/router';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Injectable()
export class NavService {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  private readonly paramMap = toSignal(this.route.paramMap);
  private readonly queryParamMap = toSignal(this.route.queryParamMap);

  public params(): ParamMap {
    return this.paramMap();
  }

  public queryParams(): ParamMap {
    return this.queryParamMap();
  }

  public param(name: string): string {
    return this.paramMap().get(name);
  }

  public queryParam(name: string): string {
    return this.queryParamMap().get(name);
  }

  public state(name: string): string {
    const state = this.router.getCurrentNavigation()?.extras.state;
    return state && state[name] ? state[name] : '';
  }

  public go(url: string) {
    this.router.navigate([url]);
  }

  public setQueryParams(queryParams: Params): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      replaceUrl: true, // do not push a new entry to the browser history
      queryParamsHandling: 'merge', // preserve other query params if there are any
    });
  }
}
