import {NgModule} from '@angular/core';
import {HomeComponent} from './home/home.component';
import {SharedModule as SharedModule} from "../shared/shared.module";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {XsrfInterceptor} from "../interceptor/xsrf.interceptor";
import {CommonModule} from "@angular/common";
import {CategoryComponent} from './category/category.component';
import {RouterModule, Routes} from "@angular/router";
import {PageGuard} from "../guard/page.guard";

const routes: Routes = [
  {path: ':name/c/:category', component: CategoryComponent, canActivate: [PageGuard]}
];

@NgModule({
  declarations: [
    HomeComponent,
    CategoryComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule,
    RouterModule.forChild(routes)
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: XsrfInterceptor,
    multi: true
  }]
})
export class CoreModule {
}
