import { Routes } from '@angular/router';
import { ChangesService } from '@app/analysis/components/changes/filter';
import { ChangesPageComponent } from './changes-page.component';

export const changesRoutes: Routes = [
  {
    path: '',
    providers: [ChangesService],
    children: [
      {
        path: '',
        component: ChangesPageComponent,
      },
    ],
  },
];
