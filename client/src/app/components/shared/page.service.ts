import {Injectable} from "@angular/core";
import {Title} from "@angular/platform-browser";
import {BehaviorSubject} from "rxjs";
import {PageWidthService} from "./page-width.service";

@Injectable({
  providedIn: "root"
})
export class PageService {

  readonly defaultTitle = "knooppuntnet";

  readonly sidebarOpen: BehaviorSubject<boolean> = new BehaviorSubject(this.sidebarOpenInitialState());

  showFooter = true;

  constructor(private pageWidthService: PageWidthService,
              private titleService: Title) {
    pageWidthService.current$.subscribe(() => this.pageWidthChanged());
  }

  defaultMenu() {
    setTimeout(() => {
      this.showFooter = true;
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
    return !(this.pageWidthService.isSmall() || this.pageWidthService.isVerySmall());
  }

}
