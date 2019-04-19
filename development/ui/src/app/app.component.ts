import {ChangeDetectorRef, Component} from "@angular/core";
import {NavigationCancel, NavigationEnd, NavigationError, NavigationStart, RouteConfigLoadEnd, RouteConfigLoadStart, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {PageService} from "./components/shared/page.service";
import {IconService} from "./icon.service";
import {UserService} from "./user.service";

@Component({
  selector: "app-root",
  template: `
    <mat-sidenav-container>

      <mat-sidenav
        [mode]="isMobile() ? 'over' : 'side'"
        [fixedInViewport]="!isMobile()"
        fixedTopGap="48"
        [opened]="isSidebarOpen()">
        <router-outlet name="sidebar"></router-outlet>
      </mat-sidenav>

      <mat-sidenav-content>
        <header>
          <kpn-toolbar></kpn-toolbar>
        </header>
        <div class="page-contents">
          <main>
            <router-outlet></router-outlet>
          </main>
          <footer>
            <kpn-page-footer></kpn-page-footer>
          </footer>
        </div>
      </mat-sidenav-content>
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

  breakPointStateSubscription: Subscription;

  constructor(private iconService: IconService,
              changeDetectorRef: ChangeDetectorRef,
              private userService: UserService,
              private pageService: PageService,
              router: Router) {
    this.breakPointStateSubscription = this.pageService.breakpointState.subscribe(() => changeDetectorRef.detectChanges());

    router.events.subscribe(event => {

      if (event instanceof RouteConfigLoadStart) {
        console.log(`DEBUG AppComponent lazy load start: '${event.route.path}'`);
        // TODO show spinner
      } else if (event instanceof RouteConfigLoadEnd) {
        console.log(`DEBUG AppComponent lazy load complete: '${event.route.path}'`);
        // TODO hide spinner
      } else if (event instanceof NavigationStart) {
        console.log(`DEBUG AppComponent navigation start: '${event}'`);
        // TODO show spinner
      } else if (event instanceof NavigationEnd) {
        console.log(`DEBUG AppComponent navigation end: '${event}'`);
        // TODO hide spinner
      } else if (event instanceof NavigationCancel) {
        console.log(`DEBUG AppComponent navigation cancel: '${event}'`);
        // TODO hide spinner
      } else if (event instanceof NavigationError) {
        console.log(`DEBUG AppComponent navigation error: '${event}'`);
        // TODO hide spinner
      }
    })
  }

  ngOnDestroy(): void {
    this.breakPointStateSubscription.unsubscribe();
  }

  isMobile(): boolean {
    return this.pageService.isMobile();
  }

  isSidebarOpen(): boolean {
    return this.pageService.isSidebarOpen();
  }

}
