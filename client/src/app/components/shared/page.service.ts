import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {Subset} from "../../kpn/shared/subset";
import {PageWidthService} from "./page-width.service";
import {Title} from "@angular/platform-browser";

@Injectable({
  providedIn: "root"
})
export class PageService {

  readonly defaultTitle = "knooppuntnet";

  readonly sidebarOpen: BehaviorSubject<boolean> = new BehaviorSubject(this.sidebarOpenInitialState());

  showSubsetsMenu: boolean = false;
  showSubsetMenu: boolean = false;
  showNetworkMenu: boolean = false;
  subset: Subset = null;
  networkId: string = null;
  showFooter: boolean = true;

  constructor(private pageWidthService: PageWidthService,
              private titleService: Title) {
    pageWidthService.current.subscribe(() => this.pageWidthChanged());
  }

  defaultMenu() {
    setTimeout(() => {
      this.showSubsetsMenu = false;
      this.showSubsetMenu = false;
      this.showNetworkMenu = false;
      this.showFooter = true;
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

  isShowFooter(): boolean {
    return this.showFooter;
  }

  setTitle(prefix: string): void {
    const title = prefix ? prefix + " | " + this.defaultTitle : this.defaultTitle;
    this.titleService.setTitle(title);
  }

  private pageWidthChanged(): void {
    const sidebarOpen = this.sidebarOpenInitialState();
    if (this.sidebarOpen.value !== sidebarOpen) {
      this.sidebarOpen.next(sidebarOpen);
    }
  }

  private sidebarOpenInitialState(): boolean {
    return !this.pageWidthService.isSmall();
  }

}
