import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '@app/shared/user/user.service';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'ui-osm-link-user',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ui-osm-link kind="user" [elementId]="user()" [title]="user()" /> `,
  imports: [OsmLinkComponent],
})
export class OsmLinkUserComponent {
  private readonly userService = inject(UserService);
  readonly user = this.userService.user;
}
