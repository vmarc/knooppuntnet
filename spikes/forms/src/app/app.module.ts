import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {Page1Component} from './page1/page1.component';
import {MenuComponent} from './shared/menu.component';
import {Page2Component} from './page2/page2.component';
import {Page3Component} from './page3/page3.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {ReactiveFormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatTableModule} from '@angular/material/table';
import {FieldErrorsComponent} from './shared/field-errors.component';
import {FormErrorsComponent} from './shared/form-errors.component';
import {InputDirective} from './shared/input.directive';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MAT_SNACK_BAR_DEFAULT_OPTIONS} from '@angular/material/snack-bar';
import {MessageService} from './shared/message.service';
import {ClassesComponent} from './shared/classes.component';
import {Sub1Component} from './page4/sub1.component';
import {Page4Component} from './page4/page4.component';
import {Sub3Component} from './page4/sub3.component';
import {Sub2Component} from './page4/sub2.component';
import {MatRadioModule} from '@angular/material/radio';
import {Page5Component} from "./page5/page5.component";
import {Sub5Component} from "./page5/sub5.component";

@NgModule({
  declarations: [
    AppComponent,
    FormErrorsComponent,
    FieldErrorsComponent,
    InputDirective,
    ClassesComponent,
    MenuComponent,
    Page1Component,
    Page2Component,
    Page3Component,
    Page4Component,
    Page5Component,
    Sub1Component,
    Sub2Component,
    Sub3Component,
    Sub5Component,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatSnackBarModule,
    MatRadioModule,
  ],
  providers: [
    MessageService,
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: {
        duration: 1500,
        verticalPosition: 'top',
        horizontalPosition: 'right',
      }
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
