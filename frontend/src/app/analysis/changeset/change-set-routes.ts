import { Routes } from '@angular/router';
import { ChangeSetPageComponent } from './change-set-page.component';

export const changeSetRoutes: Routes = [
  {
    path: ':changeSetId/:replicationNumber',
    component: ChangeSetPageComponent,
  },
];
