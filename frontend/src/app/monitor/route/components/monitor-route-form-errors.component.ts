import { Component } from '@angular/core';
import { input } from '@angular/core';

@Component({
  selector: 'kpn-monitor-route-form-errors',
  template: `
    @for (error of errors(); track $index) {
      <p class="kpn-error error">
        @switch (error) {
          @case ('no-relation-id') {
            <span i18n="@@monitor.route.save-dialog.no-relation-id">
              Note: we cannot yet perform an analysis. The reference information is still
              incomplete. The relation id has not been specified.
            </span>
          }

          @case ('osm-relation-not-found') {
            <span i18n="@@monitor.route.save-dialog.osm-relation-not-found">
              Note: we cannot yet perform an analysis. The reference information is still
              incomplete. No route with given relation id was found at given reference date.
            </span>
          }

          @case ('invalid-reference-file') {
            <span i18n="@@monitor.route.save-dialog.invalid-reference-file">
              Invalid reference file (needs to be a valid GPX file).
            </span>
          }

          @default {
            {{ error }}
          }
        }
      </p>
    }
  `,
  styles: `
    .error {
       margin-left: 2rem;
       margin-bottom: 2rem;
     }
  `,
  standalone: true,
})
export class MonitorRouteFormErrorsComponent {
  errors = input.required<string[]>();
}
