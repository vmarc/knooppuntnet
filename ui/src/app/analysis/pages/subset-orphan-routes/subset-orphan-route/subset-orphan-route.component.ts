import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'kpn-subset-orphan-route',
  templateUrl: './subset-orphan-route.component.html',
  styleUrls: ['./subset-orphan-route.component.scss']
})
export class SubsetOrphanRouteComponent implements OnInit {

  @Input() route;

  ngOnInit() {
  }

  extraTags() {
    // TODO Tags(RouteTagFilter(route).extraTags)
    return this.route.tags;
  }

}
