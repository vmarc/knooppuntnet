import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserStore } from '../../../user/user.store';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'kpn-osm-link-user',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-osm-link kind="user" [elementId]="user()" [title]="user()" /> `,
  standalone: true,
  imports: [OsmLinkComponent, AsyncPipe],
})
export class OsmLinkUserComponent {
  private readonly userStore = inject(UserStore);
  readonly user = this.userStore.user;
}
