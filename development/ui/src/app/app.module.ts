import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NgModule} from '@angular/core';
import {LayoutModule} from '@angular/cdk/layout';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {AppService} from './app.service';
import {AuthenticatePageComponent} from './pages/authenticate/_page/authenticate-page.component';
import {LoginPageComponent} from './pages/login/_page/login-page.component';
import {LogoutPageComponent} from './pages/logout/_page/logout-page.component';
import {HomePageComponent} from './pages/home/_page/home-page.component';
import {NotFoundPageComponent} from './pages/not-found/_page/not-found-page.component';
import {AboutPageComponent} from './pages/about/_page/about-page.component';
import {GlossaryPageComponent} from './pages/glossary/_page/glossary-page.component';
import {LinksPageComponent} from './pages/links/_page/links-page.component';
import {KpnMaterialModule} from "./material/kpn-material.module";
import {AppRoutingModule} from './app-routing.module';
import {SharedModule} from "./shared/shared.module";

@NgModule({
  declarations: [
    AppComponent,
    AboutPageComponent,
    AuthenticatePageComponent,
    GlossaryPageComponent,
    HomePageComponent,
    LinksPageComponent,
    LoginPageComponent,
    LogoutPageComponent,
    NotFoundPageComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    LayoutModule,
    KpnMaterialModule,
    HttpClientModule,
    SharedModule,
    AppRoutingModule
  ],
  providers: [
    AppService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
