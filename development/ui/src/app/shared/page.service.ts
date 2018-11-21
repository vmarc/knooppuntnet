import {Injectable} from '@angular/core';
import {BreakpointObserver, BreakpointState} from "@angular/cdk/layout";
import {Observable} from "rxjs";
import {Subset} from "../kpn/shared/subset";

@Injectable({
  providedIn: 'root'
})
export class PageService {

  breakpointState: Observable<BreakpointState>;

  private mobile: boolean;
  private sideNavOpen: boolean;

  showSubsetsMenu: boolean = false;
  showSubsetMenu: boolean = false;
  showNetworkMenu: boolean = false;
  subset: Subset = null;
  networkId: string = null;

  constructor(breakpointObserver: BreakpointObserver) {
    const mediaQuery = '(max-width: 599px)';
    this.updateMobile(breakpointObserver.isMatched(mediaQuery));
    this.breakpointState = breakpointObserver.observe(mediaQuery);
    this.breakpointState.subscribe((b) => this.breakpointStateChanged(b));
  }

  defaultMenu() {
    setTimeout(() => {
      this.showSubsetsMenu = false;
      this.showSubsetMenu = false;
      this.showNetworkMenu = false;
      this.subset = null;
      this.networkId = null;
    });
  }

  initSubsetPage() {
    setTimeout(() => {
      this.showSubsetsMenu = true;
      this.showSubsetMenu = true;
      this.showNetworkMenu = false;
    });
  }

  initNetworkPage() {
    setTimeout(() => {
      this.showSubsetsMenu = true;
      this.showSubsetMenu = false;
      this.showNetworkMenu = true;
    });
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
