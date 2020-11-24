import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {StoreModule} from '@ngrx/store';
import {environment} from '../environments/environment';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {CounterComponent} from './counter/counter.component';
import {NewIssueComponent} from './issue/new-issue.component';
import {ReactiveFormsModule} from '@angular/forms';
import {IssueListComponent} from './issue/issue-list.component';
import {IssuesComponent} from './issue/issues.component';
import {IssueDetailComponent} from './issue/issue-detail.component';
import {reducers} from './store/app.state';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CounterComponent,
    IssuesComponent,
    NewIssueComponent,
    IssueListComponent,
    IssueDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    StoreModule.forRoot(
      reducers,
      {
        runtimeChecks: {
          strictStateImmutability: true,
          strictActionImmutability: true,
          strictStateSerializability: true,
          strictActionSerializability: true,
          strictActionWithinNgZone: true
        }
      }
    ),
    environment.production
      ? []
      : StoreDevtoolsModule.instrument({
        name: 'ngrx test'
      }),
    StoreDevtoolsModule.instrument({maxAge: 25, logOnly: environment.production}),
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
