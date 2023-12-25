import { Routes } from '@angular/router';
import { AuthenticatedPageComponent } from './pages/authenticate/authenticated-page.component';
import { HomePageComponent } from './pages/home/home-page.component';
import { LoginPageComponent } from './pages/login/login-page.component';
import { LogoutPageComponent } from './pages/logout/logout-page.component';
import { NotFoundPageComponent } from './pages/not-found/not-found-page.component';

export const baseRoutes: Routes = [
  {
    path: '',
    component: HomePageComponent,
  },
  {
    path: 'authenticated',
    component: AuthenticatedPageComponent,
  },
  {
    path: 'login',
    component: LoginPageComponent,
  },
  {
    path: 'logout',
    component: LogoutPageComponent,
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
