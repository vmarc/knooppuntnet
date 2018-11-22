import {
  MatButtonModule,
  MatCheckboxModule, MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatPaginatorModule,
  MatRadioModule,
  MatSidenavModule,
  MatSlideToggleModule,
  MatSortModule,
  MatTableModule,
  MatToolbarModule,
  MatTooltipModule
} from '@angular/material';
import {NgModule} from "@angular/core";

@NgModule({
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatCheckboxModule,
    MatSidenavModule,
    MatToolbarModule,
    MatMenuModule,
    MatRadioModule,
    MatSlideToggleModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    MatIconModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule
  ],
  exports: [
    MatButtonModule,
    MatCheckboxModule,
    MatSidenavModule,
    MatToolbarModule,
    MatMenuModule,
    MatRadioModule,
    MatSlideToggleModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    MatIconModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule
  ],
})
export class KpnMaterialModule {
}
