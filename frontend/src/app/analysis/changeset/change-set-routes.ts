import { Routes } from '@angular/router';
import { ChangeSetPageComponent } from './internal/change-set-page.component';

export const changeSetRoutes: Routes = [
  {
    path: ':changeSetId/:replicationNumber',
    component: ChangeSetPageComponent,
  },
  {
    path: ':changeSetId',
    component: ChangeSetPageComponent,
  },
];
