import { TuiDropdownPositionSided } from '@taiga-ui/core';
import { TuiDropdownOpen } from '@taiga-ui/core';
import { TuiDropdownDirective } from '@taiga-ui/core';
import { TuiButton } from '@taiga-ui/core';
import { TuiRoot } from '@taiga-ui/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouteConfigLoadEnd } from '@angular/router';
import { RouteConfigLoadStart } from '@angular/router';
import { Router } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { IconService } from '@app/services';
import { Version } from '@app/services';
import { SpinnerService } from '@app/spinner';
import { Subscriptions } from '@app/util';
import { setTag } from '@sentry/angular';
import { TuiAppBarDirective } from '@taiga-ui/layout';
import { TuiAppBarComponent } from '@taiga-ui/layout';
import { RootPageComponent } from './root/root.component';
import { SettingsMenuComponent } from './root/toolbar/settings/settings-menu.component';

@Component({
  selector: 'kpn-app',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <tui-root>
      <tui-app-bar class="toolbar">
        <button
          iconStart="@tui.settings"
          title="Settings"
          tuiIconButton
          tuiSlot="left"
          type="button"
          [tuiDropdownSided]="true"
          [(tuiDropdownOpen)]="open"
          [tuiDropdown]="settingsMenuX"
        ></button>
        routes
        <a iconStart="@tui.user" title="User" tuiIconButton tuiSlot="right"></a>
      </tui-app-bar>

      <kpn-root>
        <router-outlet />
      </kpn-root>
    </tui-root>
    <ng-template #settingsMenuX let-close>
      <kpn-settings-menu />
    </ng-template>
  `,
  styles: `
    .toolbar {
      background-color: #f8f8f8;
      border-bottom: solid 1px lightgray;
    }
  `,
  imports: [
    MatSidenavModule,
    RootPageComponent,
    RouterOutlet,
    SettingsMenuComponent,
    TuiAppBarComponent,
    TuiAppBarDirective,
    TuiButton,
    TuiDropdownDirective,
    TuiDropdownOpen,
    TuiDropdownPositionSided,
    TuiRoot,
  ],
})
export class AppComponent implements OnDestroy {
  private readonly iconService = inject(IconService);
  private readonly spinnerService = inject(SpinnerService);
  private readonly router = inject(Router);

  private readonly subscriptions = new Subscriptions();
  protected open = false;
  protected readonly items = ['Edit', 'Download', 'Rename', 'Delete'];

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
