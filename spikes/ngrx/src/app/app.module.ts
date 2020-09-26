import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {AppRoutingModule} from "./app-routing.module";
import {AppComponent} from "./app.component";
import {CounterComponent} from "./counter/counter.component";
import {HomeComponent} from "./home/home.component";
import {StoreModule} from "@ngrx/store";
import {counterReducer} from "./counter/counter.reducer";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CounterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    StoreModule.forRoot({count: counterReducer})
  ],
  providers: [],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
