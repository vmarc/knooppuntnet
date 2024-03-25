import { input } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterOutlet } from '@angular/router';
import { SidebarBackComponent } from '@app/components/shared/sidebar';
import { ToolbarComponent } from '@app/components/shared/toolbar';
import { PageWidthService } from '../page-width.service';
import { PageService } from '../page.service';
import { PageExperimentalComponent } from './page-experimental.component';
import { PageFooterComponent } from './page-footer.component';

@Component({
  selector: 'kpn-page',
  template: `
    <mat-sidenav-container>
      <mat-sidenav
        [mode]="smallPage() ? 'over' : 'side'"
        [fixedInViewport]="!smallPage()"
        fixedTopGap="48"
        [opened]="sidebarOpen()"
      >
        @if (smallPage()) {
          <kpn-sidebar-back />
        }
        <kpn-page-experimental />
        <ng-content select="[sidebar]" />
      </mat-sidenav>

      <mat-sidenav-content>
        <header>
          <kpn-toolbar>
            <ng-content select="[toolbar]" />
          </kpn-toolbar>
        </header>
        <div class="page-contents">
          <main>
            <ng-content />
          </main>
          @if (showFooter()) {
            <footer>
              <kpn-page-footer />
            </footer>
          }
        </div>
      </mat-sidenav-content>
    </mat-sidenav-container>
  `,
  styles: `
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
  standalone: true,
  imports: [
    MatSidenavModule,
    PageExperimentalComponent,
    PageFooterComponent,
    RouterOutlet,
    SidebarBackComponent,
    ToolbarComponent,
  ],
})
export class PageComponent {
  showFooter = input<boolean>(true);

  private readonly pageService = inject(PageService);
  private readonly pageWidthService = inject(PageWidthService);

  protected readonly sidebarOpen = this.pageService.sidebarOpen;
  protected readonly smallPage = computed(() => this.pageWidthService.isAllSmall());
}
