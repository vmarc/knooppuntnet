import { Routes } from '@angular/router';
import { LoginComponent } from "./user/login.component";
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
    path: 'user-login',
    component: UserLoginComponent,
  },
  {
    path: 'user-logout',
    component: UserLogoutComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: '',
    component: HomeComponent,
  },
];
