import {Injectable} from '@angular/core';
import {BreakpointObserver, BreakpointState} from "@angular/cdk/layout";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PageService {

  breakpointState: Observable<BreakpointState>;

  private mobile: boolean;
  private sideNavOpen: boolean;

  constructor(breakpointObserver: BreakpointObserver) {
    const mediaQuery = '(max-width: 599px)';
    this.updateMobile(breakpointObserver.isMatched(mediaQuery));
    this.breakpointState = breakpointObserver.observe(mediaQuery);
    this.breakpointState.subscribe((b) => this.breakpointStateChanged(b));
  }

  toggleSideNavOpen(): void {
    this.sideNavOpen = !this.sideNavOpen;
  }

  isSideNavOpen(): boolean {
    return this.sideNavOpen;
  }

  isMobile(): boolean {
    return this.mobile;
  }

  private breakpointStateChanged(breakpointState: BreakpointState) {
    this.updateMobile(breakpointState.matches);
  }

  private updateMobile(mobile: boolean) {
    this.mobile = mobile;
    this.sideNavOpen = !mobile;
  }

}
