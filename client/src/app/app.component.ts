import { AsyncPipe } from '@angular/common';
import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouteConfigLoadEnd } from '@angular/router';
import { RouteConfigLoadStart } from '@angular/router';
import { Router } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { PageWidth } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { PageService } from '@app/components/shared';
import { PageFooterComponent } from '@app/components/shared/page';
import { PageExperimentalComponent } from '@app/components/shared/page';
import { SidebarBackComponent } from '@app/components/shared/sidebar';
import { ToolbarComponent } from '@app/components/shared/toolbar';
import { selectPageShowFooter } from '@app/core';
import { IconService } from '@app/services';
import { VersionService } from '@app/services';
import { SpinnerService } from '@app/spinner';
import { Subscriptions } from '@app/util';
import { Store } from '@ngrx/store';
import { setTag } from '@sentry/angular-ivy';
import { map } from 'rxjs/operators';

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
        <kpn-sidebar-back *ngIf="smallPage" />
        <kpn-page-experimental />
        <router-outlet name="sidebar" />
      </mat-sidenav>

      <mat-sidenav-content>
        <header>
          <kpn-toolbar />
        </header>
        <div class="page-contents">
          <main>
            <router-outlet />
          </main>
          <footer *ngIf="showFooter$ | async">
            <kpn-page-footer />
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
  standalone: true,
  imports: [
    MatSidenavModule,
    NgIf,
    SidebarBackComponent,
    PageExperimentalComponent,
    RouterOutlet,
    ToolbarComponent,
    PageFooterComponent,
    AsyncPipe,
  ],
})
export class AppComponent implements OnInit, OnDestroy {
  readonly showFooter$ = this.store.select(selectPageShowFooter);
  smallPage = false;
  private readonly subscriptions = new Subscriptions();

  constructor(
    private store: Store,
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
