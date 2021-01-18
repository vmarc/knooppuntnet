import {OnInit} from '@angular/core';
import {Injector} from '@angular/core';
import {Compiler} from '@angular/core';
import {ViewContainerRef} from '@angular/core';
import {ViewChild} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'app-feature-2-container',
  template: `
    <div class="feature-container">
      Feature 2 container
      <ng-template #container></ng-template>
    </div>
  `
})
export class Feature2ContainerComponent implements OnInit {

  @ViewChild('container', {read: ViewContainerRef}) container: ViewContainerRef;

  constructor(private compiler: Compiler,
              private injector: Injector) {
  }

  ngOnInit(): void {
    this.loadFeature();
  }

  private loadFeature(): void {
    import('./feature-2/feature-2.module').then(({Feature2Module}) => {
      this.compiler.compileModuleAsync(Feature2Module).then(moduleFactory => {
        const moduleRef = moduleFactory.create(this.injector);
        const componentFactory = moduleRef.instance.resolveComponent();
        this.container.createComponent(componentFactory);
      });
    });
  }
}
