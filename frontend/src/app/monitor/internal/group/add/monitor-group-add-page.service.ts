import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MonitorGroupProperties } from '@api/common/monitor/monitor-group-properties';
import { NavService } from '@app/shared/components/nav.service';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorGroupAddPageService {
  private readonly navService = inject(NavService);
  private readonly monitorService = inject(MonitorService);

  readonly name = new FormControl<string>('', {
    validators: [Validators.required, Validators.maxLength(15)],
    asyncValidators: this.monitorService.asyncGroupNameUniqueValidator(() => ''),
  });

  readonly description = new FormControl<string>('', [
    Validators.required,
    Validators.maxLength(100),
  ]);

  readonly form = new FormGroup({
    name: this.name,
    description: this.description,
  });

  add(): void {
    if (this.form.valid) {
      const properties: MonitorGroupProperties = {
        name: this.form.value.name,
        description: this.form.value.description,
      };
      this.monitorService.groupAdd(properties).subscribe(() => this.navService.go('/monitor'));
    }
  }
}
