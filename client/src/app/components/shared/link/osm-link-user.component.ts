import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'kpn-osm-link-user',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-osm-link kind="user" [elementId]="currentUser()" [title]="currentUser()"></kpn-osm-link>
  `
})
export class OsmLinkUserComponent {

  constructor(private userService: UserService) {
  }

  currentUser(): string {
    return this.userService.currentUser();
  }

}
