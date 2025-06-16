import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { RouteChangeComponent } from './route-change.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { RouterService } from '@app/shared/services/router.service';
import { RouteChangesPageService } from '../route-changes-page.service';

@Component({
  selector: 'ui-route-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-changes [service]="service">
      @for (routeChangeInfo of service.response().result.changes; track routeChangeInfo) {
        <ui-list-item [selected]="false">
          <ui-route-change [routeChangeInfo]="routeChangeInfo" />
        </ui-list-item>
      }
    </ui-changes>
  `,
  providers: [RouterService],
  imports: [FormsModule, ListItemComponent, RouteChangeComponent, ChangesComponent],
})
export class RouteChangesComponent {
  protected readonly service = inject(RouteChangesPageService);
}
