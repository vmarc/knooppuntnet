import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { RouterLink } from '@angular/router';
import { OldPageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { PreferencesService } from '@app/core';
import { SettingsSidebarComponent } from './settings-sidebar.component';

@Component({
  selector: 'kpn-settings-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li i18n="@@breadcrumb.settings">Settings</li>
      </ul>

      <kpn-page-header i18n="@@settings-page.title">Settings</kpn-page-header>

      <div class="setting">
        <mat-slide-toggle
          [checked]="service.extraLayers()"
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
      <kpn-settings-sidebar sidebar />
    </kpn-old-page>
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
  standalone: true,
  imports: [
    MatSlideToggleModule,
    OldPageComponent,
    PageHeaderComponent,
    RouterLink,
    SettingsSidebarComponent,
  ],
})
export class SettingsPageComponent {
  protected readonly service = inject(PreferencesService);

  extraLayersChanged(event: MatSlideToggleChange): void {
    this.service.setExtraLayers(event.checked);
  }
}
