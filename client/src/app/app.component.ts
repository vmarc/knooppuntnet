import {OnInit} from '@angular/core';
import {Component} from '@angular/core';
import {RouteConfigLoadEnd, RouteConfigLoadStart, Router} from '@angular/router';
import {map} from 'rxjs/operators';
import {PageWidth} from './components/shared/page-width';
import {PageWidthService} from './components/shared/page-width.service';
import {PageService} from './components/shared/page.service';
import {IconService} from './services/icon.service';
import {UserService} from './services/user.service';
import {SpinnerService} from './spinner/spinner.service';
import {Subscriptions} from './util/Subscriptions';
import {VersionService} from './services/version.service';
import * as Sentry from '@sentry/angular';

@Component({
  selector: 'app-root',
  template: `
    <mat-sidenav-container>

      <mat-sidenav
        [mode]="smallPage ? 'over' : 'side'"
        [fixedInViewport]="!smallPage"
        fixedTopGap="48"
        [opened]="isSidebarOpen()">
        <kpn-sidebar-back *ngIf="smallPage"></kpn-sidebar-back>
        <router-outlet name="sidebar"></router-outlet>
      </mat-sidenav>

      <mat-sidenav-content>
        <header>
          <kpn-toolbar></kpn-toolbar>
        </header>
        <div class="page-contents">
          <main>
            <router-outlet></router-outlet>
            <kpn-i18n></kpn-i18n>
          </main>
          <footer *ngIf="isShowFooter()">
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
export class AppComponent implements OnInit {

  smallPage = false;
  private readonly subscriptions = new Subscriptions();

  constructor(private iconService: IconService,
              private userService: UserService,
              private pageService: PageService,
              private pageWidthService: PageWidthService,
              private spinnerService: SpinnerService,
              private versionService: VersionService,
              router: Router) {

    Sentry.setTag('knooppuntnet-version', versionService.version);

    this.subscriptions.add(
      router.events.subscribe(event => {
        if (event instanceof RouteConfigLoadStart) {
          console.log(`AppComponent.RouteConfigLoadStart: ${event.route.path}`);
          this.spinnerService.start(`lazy-load-${event.route.path}`);
        } else if (event instanceof RouteConfigLoadEnd) {
          console.log(`AppComponent.RouteConfigLoadEnd: ${event.route.path}`);
          this.spinnerService.end(`lazy-load-${event.route.path}`);
        }
      },
      error => {
        console.log('AppComponent router event error: ' + error.toString());
      })
    );
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.pageWidthService.current$.pipe(
        map(pageWidth => this.isSmallPage(pageWidth))
      ).subscribe(smallPage => this.smallPage = smallPage)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  isSidebarOpen(): boolean {
    return this.pageService.isSidebarOpen();
  }

  isShowFooter(): boolean {
    return this.pageService.isShowFooter();
  }

  private isSmallPage(pageWidth: PageWidth): boolean {
    return PageWidth.small === pageWidth || PageWidth.verySmall === pageWidth || PageWidth.veryVerySmall === pageWidth;
  }
}
