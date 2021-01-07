import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule} from '@angular/router';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './home.component';

@NgModule({
  imports: [
    BrowserModule,
    RouterModule,
    AppRoutingModule,
  ],
  declarations: [
    AppComponent,
    HomeComponent,
  ],
  bootstrap: [
    AppComponent,
  ]
})
export class AppModule {
}
