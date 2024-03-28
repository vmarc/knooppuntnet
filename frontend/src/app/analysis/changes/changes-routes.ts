import { Routes } from '@angular/router';
import { ChangesPageComponent } from './changes-page.component';

export const changesRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        component: ChangesPageComponent,
      },
    ],
  },
];
