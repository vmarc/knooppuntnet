import { BreakpointObserver } from '@angular/cdk/layout';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { merge } from 'rxjs';
import { PageWidth } from './page-width';

@Injectable({
  providedIn: 'root',
})
export class PageWidthService {
  private readonly breakpointObserver = inject(BreakpointObserver);

  public current$ = new ReplaySubject<PageWidth>();

  private readonly veryVerySmallMaxWidth = 400;
  private readonly verySmallMaxWidth = 500;
  private readonly smallMaxWidth = 768;
  private readonly mediumMaxWidth = 1024;
  private readonly largeMaxWidth = 1300;

  constructor() {
    const veryVerySmallMediaQuery = `(max-width: ${this.veryVerySmallMaxWidth}px)`;
    const verySmallMediaQuery = `(max-width: ${this.verySmallMaxWidth}px)`;
    const smallMediaQuery = `(max-width: ${this.smallMaxWidth}px)`;
    const mediumMediaQuery = `(max-width: ${this.mediumMaxWidth}px)`;
    const largeMediaQuery = `(max-width: ${this.largeMaxWidth}px)`;

    const breakpointState$ = merge(
      this.breakpointObserver.observe(veryVerySmallMediaQuery),
      this.breakpointObserver.observe(verySmallMediaQuery),
      this.breakpointObserver.observe(smallMediaQuery),
      this.breakpointObserver.observe(mediumMediaQuery),
      this.breakpointObserver.observe(largeMediaQuery)
    );

    breakpointState$.subscribe(() => this.current$.next(this.currentPageWidth()));
  }

  currentPageWidth(): PageWidth {
    const width = window.innerWidth;
    if (width <= this.veryVerySmallMaxWidth) {
      return PageWidth.veryVerySmall;
    }
    if (width <= this.verySmallMaxWidth) {
      return PageWidth.verySmall;
    }
    if (width <= this.smallMaxWidth) {
      return PageWidth.small;
    }
    if (width <= this.mediumMaxWidth) {
      return PageWidth.medium;
    }
    if (width <= this.largeMaxWidth) {
      return PageWidth.large;
    }
    return PageWidth.veryLarge;
  }

  isVeryVerySmall(): boolean {
    return this.currentPageWidth() === PageWidth.veryVerySmall;
  }

  isVerySmall(): boolean {
    return this.currentPageWidth() === PageWidth.verySmall;
  }

  isSmall(): boolean {
    return this.currentPageWidth() === PageWidth.small;
  }

  isMedium(): boolean {
    return this.currentPageWidth() === PageWidth.medium;
  }

  isLarge(): boolean {
    return this.currentPageWidth() === PageWidth.large;
  }

  isVeryLarge(): boolean {
    return this.currentPageWidth() === PageWidth.veryLarge;
  }
}
