import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteDiffsAddedComponent } from './route-diffs-added.component';
import { RouteDiffsData } from './route-diffs-data';
import { RouteDiffsRemovedComponent } from './route-diffs-removed.component';
import { RouteDiffsUpdatedComponent } from './route-diffs-updated.component';

@Component({
  selector: 'ui-route-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-route-diffs-removed [data]="data()" />
    <ui-route-diffs-added [data]="data()" />
    <ui-route-diffs-updated [data]="data()" />
  `,
  imports: [RouteDiffsAddedComponent, RouteDiffsRemovedComponent, RouteDiffsUpdatedComponent],
})
export class RouteDiffsComponent {
  data = input.required<RouteDiffsData>();
}
