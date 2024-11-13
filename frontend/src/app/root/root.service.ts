import { BreakpointObserver } from '@angular/cdk/layout';
import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { merge } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RootService {
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly smallMaxWidth = 1000;

  private readonly _small = signal<boolean>(false);
  readonly small = this._small.asReadonly();

  private readonly _activePanel = signal<string>('map');
  readonly activePanel = this._activePanel.asReadonly();

  constructor() {
    const smallMediaQuery = `(max-width: ${this.smallMaxWidth}px)`;
    const breakpointState$ = merge(this.breakpointObserver.observe(smallMediaQuery));
    breakpointState$.subscribe(() => {
      const width = window.innerWidth;
      this._small.set(width <= this.smallMaxWidth);
    });
  }

  setShowTextPanel(value: string): void {
    this._activePanel.set(value);
  }
}
