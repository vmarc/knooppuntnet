import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MonitorRoutePageHeaderComponent } from './components/monitor-route-page-header.component';
import { MonitorRouteService } from './monitor-route.service';
import { PageComponent } from '@app/shared/components/page/page.component';

@Component({
  selector: 'ui-monitor-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-monitor-route-page-header />
      <router-outlet />
    </ui-page>
  `,
  imports: [MonitorRoutePageHeaderComponent, PageComponent, RouterOutlet],
})
export class MonitorRouteComponent implements OnInit {
  private monitorRouteService = inject(MonitorRouteService);
  readonly groupName = input<string>();
  readonly routeName = input<string>();
  readonly description = input<string>();

  ngOnInit(): void {
    // this.routeService.onInit(this.routeId());
    console.log(
      `groupName= ${this.groupName()}, routeName= ${this.routeName()}, description= ${this.description()}`
    );
  }
}
