import {Injectable} from "@angular/core";
import {PageWidth} from "./page-width";
import {BreakpointObserver} from "@angular/cdk/layout";
import {merge, Subject} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class PageWidthService {

  public current = new Subject<PageWidth>();

  private readonly smallMaxWidth = 768;
  private readonly mediumMaxWidth = 1024;
  private readonly largeMaxWidth = 1300;

  constructor(breakpointObserver: BreakpointObserver) {

    const smallMediaQuery = `(max-width: ${this.smallMaxWidth}px)`;
    const mediumMediaQuery = `(max-width: ${this.mediumMaxWidth}px)`;
    const largeMediaQuery = `(max-width: ${this.largeMaxWidth}px)`;

    const breakpointState = merge(
      breakpointObserver.observe(smallMediaQuery),
      breakpointObserver.observe(mediumMediaQuery),
      breakpointObserver.observe(largeMediaQuery)
    );

    breakpointState.subscribe(() => this.current.next(this.currentPageWidth()));
  }

  currentPageWidth(): PageWidth {
    const width = window.innerWidth;
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
