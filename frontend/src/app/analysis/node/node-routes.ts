import { Routes } from '@angular/router';
import { NodeChangesPageComponent } from './changes/node-changes-page.component';
import { NodeDetailsPageComponent } from './details/node-details-page.component';
import { NodeMapPageComponent } from './map/node-map-page.component';

export const nodeRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: ':nodeId',
        component: NodeDetailsPageComponent,
      },
      {
        path: ':nodeId/map',
        component: NodeMapPageComponent,
      },
      {
        path: ':nodeId/changes',
        component: NodeChangesPageComponent,
      },
    ],
  },
];
