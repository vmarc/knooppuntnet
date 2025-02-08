import { Routes } from '@angular/router';
import { LogPageComponent } from './internal/log-page.component';
import { ReplicationStatusPageComponent } from './internal/replication-status-page.component';
import { StatusPageComponent } from './internal/status-page.component';
import { SystemStatusPageComponent } from './internal/system-status-page.component';

export const statusRoutes: Routes = [
  { path: '', component: StatusPageComponent },
  {
    path: 'replication',
    component: ReplicationStatusPageComponent,
  },
  {
    path: 'replication/:period',
    component: ReplicationStatusPageComponent,
  },
  {
    path: 'replication/:period/:year',
    component: ReplicationStatusPageComponent,
  },
  {
    path: 'replication/:period/:year/:monthOrWeek',
    component: ReplicationStatusPageComponent,
  },
  {
    path: 'replication/:period/:year/:month/:day',
    component: ReplicationStatusPageComponent,
  },
  {
    path: 'replication/:period/:year/:month/:day/:hour',
    component: ReplicationStatusPageComponent,
  },
  { path: 'system', component: SystemStatusPageComponent },
  {
    path: 'system/:period',
    component: SystemStatusPageComponent,
  },
  {
    path: 'system/:period/:year',
    component: SystemStatusPageComponent,
  },
  {
    path: 'system/:period/:year/:monthOrWeek',
    component: SystemStatusPageComponent,
  },
  {
    path: 'system/:period/:year/:month/:day',
    component: SystemStatusPageComponent,
  },
  {
    path: 'system/:period/:year/:month/:day/:hour',
    component: SystemStatusPageComponent,
  },

  { path: 'log', component: LogPageComponent },
  { path: 'log/:period', component: LogPageComponent },
  { path: 'log/:period/:year', component: LogPageComponent },
  {
    path: 'log/:period/:year/:monthOrWeek',
    component: LogPageComponent,
  },
  {
    path: 'log/:period/:year/:month/:day',
    component: LogPageComponent,
  },
  {
    path: 'log/:period/:year/:month/:day/:hour',
    component: LogPageComponent,
  },
];
