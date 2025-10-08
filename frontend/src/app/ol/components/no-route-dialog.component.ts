import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { DialogComponent } from '@app/shared/components/dialog/dialog.component';

@Component({
  selector: 'ui-no-route-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-dialog>
      <div dialog-title i18n="@@no-route-dialog.title">No route</div>
      <div i18n="@@no-route-dialog.message">
        You clicked the button to zoom to fit the complete route on the map, but there is no start-
        and endnode for the route defined yet. Please plan a route first.
      </div>
    </ui-dialog>
  `,
  imports: [DialogComponent],
})
export class NoRouteDialogComponent {}
