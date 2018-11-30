import {NgModule} from '@angular/core';
import {MatCardModule, MatGridListModule} from "@angular/material";
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {LayoutModule} from '@angular/cdk/layout';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {AppService} from './app.service';
import {KpnMaterialModule} from "./material/kpn-material.module";
import {AppRoutingModule} from './app-routing.module';
import {SharedModule} from "./shared/shared.module";
import {UserService} from "./user.service";
import {CookieService} from "ngx-cookie-service";
import {MarkdownModule} from "ngx-markdown";
import {PageService} from "./shared/page.service";

@NgModule({
  declarations: [
    AppComponent
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
    CookieService,
    UserService,
    AppService,
    PageService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
