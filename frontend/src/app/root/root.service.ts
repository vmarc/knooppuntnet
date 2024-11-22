import { BreakpointObserver } from '@angular/cdk/layout';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { merge } from 'rxjs';
import { StateService } from '@app/state';

@Injectable({
  providedIn: 'root',
})
export class RootService {
  private readonly state = inject(StateService);
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly smallMaxWidth = 1000;

  constructor() {
    const smallMediaQuery = `(max-width: ${this.smallMaxWidth}px)`;
    const breakpointState$ = merge(this.breakpointObserver.observe(smallMediaQuery));
    breakpointState$.subscribe(() => {
      const width = window.innerWidth;
      this.state.page.updateSmall(width <= this.smallMaxWidth);
    });
  }
}
