import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { selectUserUser } from '@app/core';
import { Store } from '@ngrx/store';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'kpn-osm-link-user',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-osm-link kind="user" [elementId]="user()" [title]="user()" /> `,
  standalone: true,
  imports: [OsmLinkComponent, AsyncPipe],
})
export class OsmLinkUserComponent {
  readonly user = this.store.selectSignal(selectUserUser);

  constructor(private store: Store) {}
}
