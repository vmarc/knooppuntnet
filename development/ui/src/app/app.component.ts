import {ChangeDetectorRef, Component} from "@angular/core";
import {RouteConfigLoadEnd, RouteConfigLoadStart, Router} from "@angular/router";
import {PageService} from "./components/shared/page.service";
import {IconService} from "./services/icon.service";
import {SpinnerService} from "./spinner/spinner.service";
import {UserService} from "./services/user.service";
import {Subscriptions} from "./util/Subscriptions";

@Component({
  selector: "app-root",
  template: `
    <mat-sidenav-container>

      <mat-sidenav-content>
        <header>
          <kpn-toolbar></kpn-toolbar>
        </header>
        <div class="page-contents">
          <main>
            <router-outlet></router-outlet>
          </main>
          <footer *ngIf="isShowFooter()">
            <kpn-page-footer></kpn-page-footer>
          </footer>
        </div>
      </mat-sidenav-content>

      <mat-sidenav
        [mode]="isMobile() ? 'over' : 'side'"
        [fixedInViewport]="!isMobile()"
        fixedTopGap="48"
        [opened]="isSidebarOpen()">
        <router-outlet name="sidebar"></router-outlet>
      </mat-sidenav>

    </mat-sidenav-container>
  `,
  styles: [`

    header {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 48px;
    }

    .page-contents {
      margin-top: 48px;
      display: flex;
      min-height: calc(100vh - 48px);
      flex-direction: column;
    }

    main {
      flex: 1;
      margin: 20px;
    }

    mat-sidenav {
      min-width: 360px;
      max-width: 360px;
    }

  `]
})
export class AppComponent {

  private readonly subscriptions = new Subscriptions();

  constructor(private iconService: IconService,
              changeDetectorRef: ChangeDetectorRef,
              private userService: UserService,
              private pageService: PageService,
              private spinnerService: SpinnerService,
              router: Router) {

    this.subscriptions.add(this.pageService.breakpointState.subscribe(() => changeDetectorRef.detectChanges()));

    this.subscriptions.add(router.events.subscribe(event => {
      if (event instanceof RouteConfigLoadStart) {
        this.spinnerService.start(`lazy-load-${event.route.path}`);
      } else if (event instanceof RouteConfigLoadEnd) {
        this.spinnerService.end(`lazy-load-${event.route.path}`);
      }
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  isMobile(): boolean {
    return this.pageService.isMobile();
  }

  isSidebarOpen(): boolean {
    return this.pageService.isSidebarOpen();
  }

  isShowFooter(): boolean {
    return this.pageService.isShowFooter();
  }
}
