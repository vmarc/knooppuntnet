import {OnInit} from '@angular/core';
import {Injector} from '@angular/core';
import {Compiler} from '@angular/core';
import {ViewContainerRef} from '@angular/core';
import {ViewChild} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'app-feature-3-container',
  template: `
    <div class="feature-container">
      Feature 3 container
      <ng-template #container></ng-template>
    </div>
  `
})
export class Feature3ContainerComponent implements OnInit {

  @ViewChild('container', {read: ViewContainerRef}) container: ViewContainerRef;

  constructor(private compiler: Compiler,
              private injector: Injector) {
  }

  ngOnInit(): void {
    this.loadFeature();
  }

  private loadFeature(): void {
    import('./feature-3/feature-3.module').then(({Feature3Module}) => {
      this.compiler.compileModuleAsync(Feature3Module).then(moduleFactory => {
        const moduleRef = moduleFactory.create(this.injector);
        const componentFactory = moduleRef.instance.resolveComponent();
        this.container.createComponent(componentFactory);
      });
    });
  }
}
