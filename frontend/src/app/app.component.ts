import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouteConfigLoadEnd } from '@angular/router';
import { RouteConfigLoadStart } from '@angular/router';
import { Router } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { PageFooterComponent } from '@app/components/shared/page';
import { PageExperimentalComponent } from '@app/components/shared/page';
import { SidebarBackComponent } from '@app/components/shared/sidebar';
import { ToolbarComponent } from '@app/components/shared/toolbar';
import { IconService } from '@app/services';
import { VersionService } from '@app/services';
import { SpinnerService } from '@app/spinner';
import { Subscriptions } from '@app/util';
import { setTag } from '@sentry/angular-ivy';

@Component({
  selector: 'kpn-root',
  template: ` <router-outlet /> `,
  styles: `
    header {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 48px;
      z-index: 1001;
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
export class AppComponent implements OnDestroy {
  private readonly iconService = inject(IconService);
  private readonly spinnerService = inject(SpinnerService);
  private readonly versionService = inject(VersionService);
  private readonly router = inject(Router);

  private readonly subscriptions = new Subscriptions();

  constructor() {
    setTag('knooppuntnet-version', this.versionService.version);

    this.subscriptions.add(
      this.router.events.subscribe({
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

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
