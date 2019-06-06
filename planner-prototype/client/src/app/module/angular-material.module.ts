import {NgModule} from '@angular/core';
import {CommonModule} from "@angular/common";
import {
  MatButtonModule,
  MatExpansionModule,
  MatIconModule,
  MatRadioModule,
  MatTableModule,
  MatTooltipModule,
} from '@angular/material';

@NgModule({
  imports: [
    CommonModule,
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    MatRadioModule,
    MatTableModule,
    MatTooltipModule
  ],
  exports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    MatRadioModule,
    MatTooltipModule
  ]
})
export class AngularMaterialModule {
}
