import {OnInit} from '@angular/core';
import {ViewContainerRef} from '@angular/core';
import {ViewChild} from '@angular/core';
import {Injector} from '@angular/core';
import {Compiler} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'app-feature-1-container',
  template: `
    <div class="feature-container">
      Feature 1 container
      <ng-template #container></ng-template>
    </div>
  `
})
export class Feature1ContainerComponent implements OnInit {

  @ViewChild('container', {read: ViewContainerRef}) container: ViewContainerRef;

  constructor(private compiler: Compiler,
              private injector: Injector) {
  }

  ngOnInit(): void {
    this.loadFeature();
  }

  private loadFeature(): void {
    import('./feature-1/feature-1.module').then(({Feature1Module}) => {
      this.compiler.compileModuleAsync(Feature1Module).then(moduleFactory => {
        const moduleRef = moduleFactory.create(this.injector);
        const componentFactory = moduleRef.instance.resolveComponent();
        this.container.createComponent(componentFactory);
      });
    });
  }
}
