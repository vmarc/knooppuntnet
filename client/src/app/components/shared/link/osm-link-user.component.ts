import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectUserUser } from '../../../core/user/user.selectors';

@Component({
  selector: 'kpn-osm-link-user',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-osm-link
      kind="user"
      [elementId]="user$ | async"
      [title]="user$ | async"
    />
  `,
})
export class OsmLinkUserComponent {
  readonly user$ = this.store.select(selectUserUser);

  constructor(private store: Store) {}
}
