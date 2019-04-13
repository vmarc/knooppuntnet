import {BreakpointObserver, BreakpointState} from "@angular/cdk/layout";
import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";
import {Subset} from "../../kpn/shared/subset";

@Injectable({
  providedIn: "root"
})
export class PageService {

  breakpointState: Observable<BreakpointState>;
  sidebarOpen: BehaviorSubject<boolean> = new BehaviorSubject(true);

  private mobile: boolean;

  showSubsetsMenu: boolean = false;
  showSubsetMenu: boolean = false;
  showNetworkMenu: boolean = false;
  subset: Subset = null;
  networkId: string = null;

  constructor(breakpointObserver: BreakpointObserver) {
    const mediaQuery = "(max-width: 599px)";
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

  toggleSidebarOpen(): void {
    this.sidebarOpen.next(!this.sidebarOpen.value);
  }

  isSidebarOpen(): boolean {
    return this.sidebarOpen.value;
  }

  isMobile(): boolean {
    return this.mobile;
  }

  private breakpointStateChanged(breakpointState: BreakpointState) {
    this.updateMobile(breakpointState.matches);
  }

  private updateMobile(mobile: boolean) {
    this.mobile = mobile;
    if (this.sidebarOpen.value === mobile) {
      this.sidebarOpen.next(!mobile);
    }
  }

}
