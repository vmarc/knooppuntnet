import { Routes } from '@angular/router';
import { DemoPageComponent } from './demo-page.component';

export const demoRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: ':video',
        component: DemoPageComponent,
      },
      {
        path: '',
        component: DemoPageComponent,
      },
    ],
  },
];
