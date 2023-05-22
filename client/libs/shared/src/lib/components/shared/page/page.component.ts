import { AsyncPipe } from '@angular/common';
import { NgIf } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
/*
 Note: the [@.disabled]="true" in mat-sidenav-container is to disable the
 animation when opening/closing the sidebar. We disable the animation because
 this animation caused a problem with the display of tiles in the MapLibre
 layers.
*/
import { Component } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterOutlet } from '@angular/router';
import { SidebarBackComponent } from '@app/components/shared/sidebar';
import { ToolbarComponent } from '@app/components/shared/toolbar';
import { selectPageShowFooter } from '@app/core';
import { IconService } from '@app/services';
import { Subscriptions } from '@app/util';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { PageWidth } from '../page-width';
import { PageWidthService } from '../page-width.service';
import { PageService } from '../page.service';
import { PageExperimentalComponent } from './page-experimental.component';
import { PageFooterComponent } from './page-footer.component';

/*
 Note: the [@.disabled]="true" in mat-sidenav-container is to disable the
 animation when opening/closing the sidebar.
*/
@Component({
  selector: 'kpn-page',
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
        <ng-content select="[sidebar]" />
      </mat-sidenav>

      <mat-sidenav-content>
        <header>
          <kpn-toolbar />
        </header>
        <div class="page-contents">
          <main>
            <ng-content />
          </main>
          <footer *ngIf="showFooter()">
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
export class PageComponent implements OnInit, OnDestroy {
  readonly showFooter = this.store.selectSignal(selectPageShowFooter);
  smallPage = false;
  private readonly subscriptions = new Subscriptions();

  constructor(
    private store: Store,
    private iconService: IconService,
    private pageService: PageService,
    private pageWidthService: PageWidthService
  ) {}

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
