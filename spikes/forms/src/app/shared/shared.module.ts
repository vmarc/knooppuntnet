import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { RouterModule } from '@angular/router';
import { ClassesComponent } from './classes.component';
import { FieldErrorsComponent } from './field-errors.component';
import { FormErrorsComponent } from './form-errors.component';
import { InputDirective } from './input.directive';
import { MenuComponent } from './menu.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatSnackBarModule,
    MatRadioModule,
    MatCheckboxModule,
  ],
  declarations: [
    InputDirective,
    FormErrorsComponent,
    FieldErrorsComponent,
    ClassesComponent,
    MenuComponent,
  ],
  exports: [
    InputDirective,
    FormErrorsComponent,
    FieldErrorsComponent,
    ClassesComponent,
    MenuComponent,
  ],
  providers: [],
})
export class SharedModule {}
