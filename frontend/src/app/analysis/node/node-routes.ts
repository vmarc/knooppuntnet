import { Routes } from '@angular/router';
import { NodeComponent } from '@app/analysis/node/internal/node.component';
import { NodeChangesPageComponent } from './internal/changes/node-changes-page.component';
import { NodeDetailsPageComponent } from './internal/details/node-details-page.component';

export const nodeRoutes: Routes = [
  {
    path: ':nodeId',
    component: NodeComponent,
    children: [
      {
        path: '',
        component: NodeDetailsPageComponent,
      },
      {
        path: 'changes',
        component: NodeChangesPageComponent,
      },
    ],
  },
];
