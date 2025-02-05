import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { RouterLink } from '@angular/router';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { State } from '@app/state';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { PageComponent } from '../shared/components/page/page.component';

@Component({
  selector: 'kpn-settings-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.settings">Settings</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <kpn-page-header i18n="@@settings-page.title">Settings</kpn-page-header>

      <div class="setting">
        <mat-slide-toggle
          [checked]="extraLayers()"
          (change)="extraLayersChanged($event)"
          i18n="@@settings.extra-layers"
        >
          Extra layers
        </mat-slide-toggle>

        <p class="comment" i18n="@@settings.extra-layers.comment.1">
          Enables the option to select extra layers in the planner map.
        </p>
        <p class="comment" i18n="@@settings.extra-layers.comment.2">
          These extra layers are intended for debugging purposes only, and are not needed for normal
          planner use. The extra layers can show the tile names and background tiles from
          OpenStreetMap (normally we use tiles from our own server).
        </p>
      </div>
    </kpn-page>
  `,
  styles: `
    .setting {
      margin-bottom: 3em;
    }

    .comment {
      margin-left: 1em;
      max-width: 40em;
      font-style: italic;
    }
  `,
  imports: [
    MatSlideToggleModule,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class SettingsPageComponent {
  private readonly state = inject(State);
  readonly extraLayers = this.state.preferences.extraLayers;

  extraLayersChanged(event: MatSlideToggleChange): void {
    this.state.preferences.updateExtraLayers(event.checked);
  }
}
