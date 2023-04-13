import { inject, NgModule } from '@angular/core';
import { HomeComponent } from './home/home.component';
import { SharedModule as SharedModule } from "../shared/shared.module";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { XsrfInterceptor } from "../interceptor/xsrf.interceptor";
import { CommonModule, NgOptimizedImage } from "@angular/common";
import { CategoryComponent } from './category/category.component';
import { RouterModule, Routes } from "@angular/router";
import { PageGuard } from "../guard/page.guard";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { EffectsModule } from "@ngrx/effects";
import { UserEffects } from "../store/effect/user.effect";

const routes: Routes = [
  { path: ':name/c/:category', component: CategoryComponent, canActivate: [() => inject(PageGuard).canActivate()] }
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
        RouterModule.forChild(routes),
        FontAwesomeModule,
        EffectsModule.forFeature(UserEffects),
        NgOptimizedImage
    ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: XsrfInterceptor,
    multi: true
  }]
})
export class CoreModule {
}
