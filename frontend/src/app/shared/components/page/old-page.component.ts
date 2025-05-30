import { input } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SidebarBackComponent } from '@app/shared/components/sidebar/sidebar-back.component';
import { PageWidthService } from '../page-width.service';
import { PageService } from '../page.service';
import { PageExperimentalComponent } from './page-experimental.component';
import { PageFooterComponent } from './page-footer.component';

@Component({
  selector: 'ui-old-page',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <mat-sidenav-container>
      <mat-sidenav
        [mode]="smallPage() ? 'over' : 'side'"
        [fixedInViewport]="!smallPage()"
        fixedTopGap="48"
        [opened]="sidebarOpen()"
      >
        @if (smallPage()) {
          <ui-sidebar-back />
        }
        <ui-page-experimental />
        <ng-content select="[sidebar]" />
      </mat-sidenav>

      <mat-sidenav-content>
        <header>
          <!--          <ui-toolbar>-->
          <!--            <ng-content select="[toolbar]" />-->
          <!--          </ui-toolbar>-->
        </header>
        <div class="page-contents">
          <main>
            <ng-content />
          </main>
          @if (showFooter()) {
            <footer>
              <ui-page-footer />
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
  imports: [MatSidenavModule, PageExperimentalComponent, PageFooterComponent, SidebarBackComponent],
})
export class OldPageComponent {
  showFooter = input<boolean>(true);

  private readonly pageService = inject(PageService);
  private readonly pageWidthService = inject(PageWidthService);

  readonly sidebarOpen = this.pageService.sidebarOpen;
  readonly smallPage = computed(() => this.pageWidthService.isAllSmall());
}
