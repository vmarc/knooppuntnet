import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { SharedModule } from '../../components/shared/shared.module';
import { AnalysisStrategyComponent } from './analysis-strategy.component';
import { AnalysisStrategyService } from './analysis-strategy.service';

@NgModule({
  imports: [CommonModule, SharedModule, MatRadioModule, MatIconModule],
  declarations: [AnalysisStrategyComponent],
  exports: [AnalysisStrategyComponent],
  providers: [AnalysisStrategyService],
})
export class AnalysisStrategyModule {}
