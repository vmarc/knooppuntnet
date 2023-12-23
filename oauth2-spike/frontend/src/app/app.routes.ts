import { Routes } from '@angular/router';
import { UserAuthenticatedComponent } from './user/user-authenticated.component';
import { HomeComponent } from './pages/home.component';
import { UserLoginComponent } from './user/user-login.component';
import { UserLogoutComponent } from './user/user-logout.component';
import { Page1Component } from './pages/page1.component';
import { Page2Component } from './pages/page2.component';
import { Page3Component } from './pages/page3.component';

export const routes: Routes = [
  {
    path: 'page1',
    component: Page1Component,
  },
  {
    path: 'page2',
    component: Page2Component,
  },
  {
    path: 'page3',
    component: Page3Component,
  },
  {
    path: 'login',
    component: UserLoginComponent,
  },
  {
    path: 'logout',
    component: UserLogoutComponent,
  },
  {
    path: 'authenticated',
    component: UserAuthenticatedComponent,
  },
  {
    path: '',
    component: HomeComponent,
  },
];
