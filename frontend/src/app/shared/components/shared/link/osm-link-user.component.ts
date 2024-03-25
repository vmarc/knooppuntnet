import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '../../../user';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'kpn-osm-link-user',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-osm-link kind="user" [elementId]="user()" [title]="user()" /> `,
  standalone: true,
  imports: [OsmLinkComponent],
})
export class OsmLinkUserComponent {
  private readonly userService = inject(UserService);
  readonly user = this.userService.user;
}
