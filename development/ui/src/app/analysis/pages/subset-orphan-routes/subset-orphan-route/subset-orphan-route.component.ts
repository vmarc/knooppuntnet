import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-subset-orphan-route',
  templateUrl: './subset-orphan-route.component.html',
  styleUrls: ['./subset-orphan-route.component.scss']
})
export class SubsetOrphanRouteComponent {

  @Input() route;

  extraTags() {
    // TODO Tags(RouteTagFilter(route).extraTags)
    return this.route.tags;
  }

}
