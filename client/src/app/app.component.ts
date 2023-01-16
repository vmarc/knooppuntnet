import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { RouteConfigLoadEnd } from '@angular/router';
import { RouteConfigLoadStart } from '@angular/router';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { setTag } from '@sentry/angular';
import { map } from 'rxjs/operators';
import { PageWidth } from './components/shared/page-width';
import { PageWidthService } from './components/shared/page-width.service';
import { PageService } from './components/shared/page.service';
import { AppState } from './core/core.state';
import { selectPageShowFooter } from './core/page/page.selectors';
import { IconService } from './services/icon.service';
import { VersionService } from './services/version.service';
import { SpinnerService } from './spinner/spinner.service';
import { Subscriptions } from './util/Subscriptions';

/*
 Note: the [@.disabled]="true" in mat-sidenav-container is to disable the
 animation when opening/closing the sidebar. We disable the animation because
 this animation caused a problem with the display of tiles in the MapLibre
 layers.
*/
@Component({
  selector: 'kpn-root',
  template: `
    <mat-sidenav-container [@.disabled]="true">
      <mat-sidenav
        [mode]="smallPage ? 'over' : 'side'"
        [fixedInViewport]="!smallPage"
        fixedTopGap="48"
        [opened]="isSidebarOpen()"
      >
        <kpn-sidebar-back *ngIf="smallPage"/>
        <kpn-page-experimental/>
        <router-outlet name="sidebar"/>
      </mat-sidenav>

      <mat-sidenav-content>
        <header>
          <kpn-toolbar/>
        </header>
        <div class="page-contents">
          <main>
            <router-outlet/>
          </main>
          <footer *ngIf="showFooter$ | async">
            <kpn-page-footer/>
          </footer>
        </div>
      </mat-sidenav-content>
    </mat-sidenav-container>
  `,
  styles: [
    `
      header {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 48px;
        z-index: 1001;
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
    `,
  ],
})
export class AppComponent implements OnInit, OnDestroy {
  readonly showFooter$ = this.store.select(selectPageShowFooter);
  smallPage = false;
  private readonly subscriptions = new Subscriptions();

  constructor(
    private store: Store<AppState>,
    private iconService: IconService,
    private pageService: PageService,
    private pageWidthService: PageWidthService,
    private spinnerService: SpinnerService,
    private versionService: VersionService,
    router: Router
  ) {
    setTag('knooppuntnet-version', versionService.version);

    this.subscriptions.add(
      router.events.subscribe({
        next: (event) => {
          if (event instanceof RouteConfigLoadStart) {
            this.spinnerService.start(`lazy-load-${event.route.path}`);
          } else if (event instanceof RouteConfigLoadEnd) {
            this.spinnerService.end(`lazy-load-${event.route.path}`);
          }
        },
        error: (error) => {
          console.log('AppComponent router event error: ' + error.toString());
        },
      })
    );
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.pageWidthService.current$
        .pipe(map((pageWidth) => this.isSmallPage(pageWidth)))
        .subscribe((smallPage) => (this.smallPage = smallPage))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  isSidebarOpen(): boolean {
    return this.pageService.isSidebarOpen();
  }

  private isSmallPage(pageWidth: PageWidth): boolean {
    return (
      PageWidth.small === pageWidth ||
      PageWidth.verySmall === pageWidth ||
      PageWidth.veryVerySmall === pageWidth
    );
  }
}
