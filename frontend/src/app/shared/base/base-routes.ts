import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home/home-page.component';
import { NotFoundPageComponent } from './pages/not-found/not-found-page.component';

export const baseRoutes: Routes = [
  {
    path: '',
    component: HomePageComponent,
  },
  {
    path: 'not-found',
    component: NotFoundPageComponent,
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];
