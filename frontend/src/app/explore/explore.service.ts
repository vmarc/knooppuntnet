import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { State } from '@app/state';

@Injectable({
  providedIn: 'root',
})
export class ExploreService {
  private readonly state = inject(State);
  readonly routes = this.state.explore.routes;
}
