import {NgModule} from '@angular/core';
import {HomeComponent} from './home/home.component';
import {SharedModule as SharedModule} from "../shared/shared.module";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {XsrfInterceptor} from "../interceptor/xsrf-interceptor";
import {CommonModule} from "@angular/common";

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: XsrfInterceptor,
    multi: true
  }]
})
export class CoreModule {
}
