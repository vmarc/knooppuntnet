import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { signal } from '@angular/core';
import { WritableSignal } from '@angular/core';
import { Injectable } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ParamMap } from '@angular/router';
import { Params } from '@angular/router';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Nav } from './nav';

@Injectable()
export class NavService {
  private readonly paramMap;
  private readonly queryParamMap;

  constructor(private router: Router, private route: ActivatedRoute) {
    this.paramMap = toSignal(this.route.paramMap);
    this.queryParamMap = toSignal(this.route.queryParamMap);
  }

  nav(): Nav {
    const state = this.router.getCurrentNavigation()?.extras.state;
    return new Nav(this.paramMap(), this.queryParamMap(), state);
  }

  public params(): ParamMap {
    return this.paramMap();
  }

  public queryParams(): ParamMap {
    return this.queryParamMap();
  }

  public newParam(name: string): string {
    return this.paramMap().get(name);
  }

  public param(name: string): Signal<string> {
    return computed(() => this.paramMap().get(name));
  }

  public queryParam(name: string): Signal<string> {
    return computed(() => this.queryParamMap().get(name));
  }

  public newQueryParam(name: string): string {
    return this.queryParamMap().get(name);
  }

  public state(name: string): WritableSignal<string> {
    const state = this.router.getCurrentNavigation()?.extras.state;
    const value = state && state[name] ? state[name] : '';
    return signal(value);
  }

  public newState(name: string): string {
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
