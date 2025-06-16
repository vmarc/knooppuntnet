import { ChangeDetectionStrategy, Component, inject, input } from '@angular/core';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { LocationChangesPageService } from '../location-changes-page.service';
import { LocationChangeComponent } from './location-change.component';
import { LocationChangesPage } from '@api/common/location/location-changes-page';

@Component({
  selector: 'ui-location-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-changes [service]="service">
      @for (changeSet of page().changeSets; track $index) {
        <ui-list-item [selected]="false">
          <ui-location-change [changeSet]="changeSet" />
        </ui-list-item>
      }
    </ui-changes>
  `,
  imports: [LocationChangeComponent, ChangesComponent, ListItemComponent],
})
export class LocationChangesComponent {
  protected readonly service = inject(LocationChangesPageService);

  readonly page = input.required<LocationChangesPage>();
}
