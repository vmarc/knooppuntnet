import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { State } from '@app/state/state';
import { PageComponent } from '../shared/components/page/page.component';

@Component({
  selector: 'ui-settings-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-page-header i18n="@@settings-page.title">Settings</ui-page-header>

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
    </ui-page>
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
  imports: [MatSlideToggleModule, PageComponent, PageHeaderComponent, BreadcrumbComponent],
})
export class SettingsPageComponent {
  private readonly state = inject(State);
  protected readonly extraLayers = this.state.preferences.extraLayers;
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    { label: Breadcrumbs.settingsLabel },
  ];

  extraLayersChanged(event: MatSlideToggleChange): void {
    this.state.preferences.updateExtraLayers(event.checked);
  }
}
