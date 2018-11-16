import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NgModule} from '@angular/core';
import {LayoutModule} from '@angular/cdk/layout';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {AppService} from './app.service';
import {AuthenticatePageComponent} from './pages/authenticate/authenticate-page.component';
import {LoginPageComponent} from './pages/login/login-page.component';
import {LogoutPageComponent} from './pages/logout/logout-page.component';
import {HomePageComponent} from './pages/home/home-page.component';
import {NotFoundPageComponent} from './pages/not-found/not-found-page.component';
import {AboutPageComponent} from './pages/about/about-page.component';
import {GlossaryPageComponent} from './pages/glossary/glossary-page.component';
import {LinksPageComponent} from './pages/links/links-page.component';
import {KpnMaterialModule} from "./material/kpn-material.module";
import {AppRoutingModule} from './app-routing.module';
import {SharedModule} from "./shared/shared.module";
import {UserService} from "./user.service";
import {CookieService} from "ngx-cookie-service";
import {MarkdownModule, MarkdownService} from "ngx-markdown";

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
    MarkdownModule.forRoot(),
    BrowserModule,
    BrowserAnimationsModule,
    LayoutModule,
    KpnMaterialModule,
    HttpClientModule,
    SharedModule,
    AppRoutingModule
  ],
  providers: [
 //   MarkdownService,
    CookieService,
    UserService,
    AppService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
