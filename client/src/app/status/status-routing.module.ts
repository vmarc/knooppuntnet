import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {Util} from "../components/shared/util";
import {ReplicationStatusPageComponent} from "./status/replication-status-page.component";
import {StatusPageComponent} from "./status/status-page.component";
import {StatusSidebarComponent} from "./status/status-sidebar.component";

const routes: Routes = [
  Util.routePath("", StatusPageComponent, StatusSidebarComponent),
  Util.routePath("replication", ReplicationStatusPageComponent, StatusSidebarComponent),
  Util.routePath("replication/:period", ReplicationStatusPageComponent, StatusSidebarComponent),
  Util.routePath("replication/:period/:year", ReplicationStatusPageComponent, StatusSidebarComponent),
  Util.routePath("replication/:period/:year/:monthOrWeek", ReplicationStatusPageComponent, StatusSidebarComponent),
  Util.routePath("replication/:period/:year/:month/:day", ReplicationStatusPageComponent, StatusSidebarComponent),
  Util.routePath("replication/:period/:year/:month/:day/:hour", ReplicationStatusPageComponent, StatusSidebarComponent),
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class StatusRoutingModule {
}
