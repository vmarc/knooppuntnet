import { signal } from '@angular/core';
import { WritableSignal } from '@angular/core';
import { Signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { map } from 'rxjs/operators';

@Injectable()
export class NavService {
  constructor(private router: Router, private route: ActivatedRoute) {}

  public param(name: string): Signal<string> {
    return toSignal(this.route.paramMap.pipe(map((m) => m.get(name))));
  }

  public queryParam(name: string): Signal<string> {
    return toSignal(this.route.queryParamMap.pipe(map((m) => m.get(name))));
  }

  public state(name: string): WritableSignal<string> {
    const state = this.router.getCurrentNavigation().extras.state;
    const value = state && state[name] ? state[name] : '';
    return signal(value);
  }

  public go(url: string) {
    this.router.navigate([url]);
  }
}
