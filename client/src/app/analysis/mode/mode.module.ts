import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { SharedModule } from '../../components/shared/shared.module';
import { AnalysisModeComponent } from './analysis-mode.component';
import { AnalysisModeService } from './analysis-mode.service';

@NgModule({
  imports: [CommonModule, SharedModule, MatRadioModule, MatIconModule],
  declarations: [AnalysisModeComponent],
  exports: [AnalysisModeComponent],
  providers: [AnalysisModeService],
})
export class AnalysisModeModule {}
