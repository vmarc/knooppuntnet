import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { AppState } from '../core/core.state';
import { Store } from '@ngrx/store';
import { select } from '@ngrx/store';
import { selectPreferencesInstructions } from '../core/preferences/preferences.selectors';
import { selectPreferencesExtraLayers } from '../core/preferences/preferences.selectors';
import { actionPreferencesInstructions } from '../core/preferences/preferences.actions';
import { actionPreferencesExtraLayers } from '../core/preferences/preferences.actions';

@Component({
  selector: 'kpn-settings-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li i18n="@@breadcrumb.settings">Settings</li>
    </ul>

    <kpn-page-header i18n="@@settings-page.title">Settings</kpn-page-header>

    <div class="setting">
      <mat-slide-toggle
        [checked]="instructions$ | async"
        (change)="instructionsChanged($event)"
        i18n="@@settings.directions"
      >
        Navigation instructions
      </mat-slide-toggle>
      <p class="comment" i18n="@@settings.directions.comment.1">
        You can enable this extra functionality in the planner to generate a
        list with navigation instructions for the route you have planned.
      </p>
      <p class="comment" i18n="@@settings.directions.comment.2">
        This functionality is still experimental and under development at this
        moment and may not work completely ok yet. By default, this
        functionality is not enabled.
      </p>
    </div>

    <div class="setting">
      <mat-slide-toggle
        [checked]="extraLayers$ | async"
        (change)="extraLayersChanged($event)"
        i18n="@@settings.extra-layers"
      >
        Extra layers
      </mat-slide-toggle>

      <p class="comment" i18n="@@settings.extra-layers.comment.1">
        Enables the option to select extra layers in the planner map.
      </p>
      <p class="comment" i18n="@@settings.extra-layers.comment.2">
        These extra layers are intended for debugging purposes only, and are not
        needed for normal planner use. The extra layers can show the tile names
        and background tiles from OpenStreetMap (normally we use tiles from our
        own server).
      </p>
    </div>
  `,
  styles: [
    `
      .setting {
        margin-bottom: 3em;
      }

      .comment {
        margin-left: 1em;
        max-width: 40em;
        font-style: italic;
      }

      .spacer {
        margin-top: 50px;
      }
    `,
  ],
})
export class SettingsPageComponent {
  readonly instructions$ = this.store.pipe(
    select(selectPreferencesInstructions)
  );
  readonly extraLayers$ = this.store.pipe(select(selectPreferencesExtraLayers));

  constructor(private store: Store<AppState>) {}

  instructionsChanged(event: MatSlideToggleChange): void {
    this.store.dispatch(
      actionPreferencesInstructions({ instructions: event.checked })
    );
  }

  extraLayersChanged(event: MatSlideToggleChange): void {
    this.store.dispatch(
      actionPreferencesExtraLayers({ extraLayers: event.checked })
    );
  }
}
