import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { RouteConfigLoadEnd } from '@angular/router';
import { RouteConfigLoadStart } from '@angular/router';
import { Router } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { IconService } from '@app/shared/services/icon.service';
import { Version } from '@app/shared/services/version';
import { SpinnerService } from '@app/shared/spinner/spinner.service';
import { Subscriptions } from '@app/util/subscriptions';
import { setTag } from '@sentry/angular';
import { RootPageComponent } from './root/internal/root.component';

@Component({
  selector: 'kpn-app',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-root>
      <router-outlet />
    </kpn-root>
  `,
  imports: [RootPageComponent, RouterOutlet],
})
export class AppComponent implements OnDestroy {
  private readonly iconService = inject(IconService);
  private readonly spinnerService = inject(SpinnerService);
  private readonly router = inject(Router);

  private readonly subscriptions = new Subscriptions();

  constructor() {
    setTag('knooppuntnet-version', Version.id);

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
