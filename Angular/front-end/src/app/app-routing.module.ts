import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./core/home/home.component";
import { PageGuard } from "./guard/page.guard";

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./authentication/authentication.module').then(m => m.AuthenticationModule)
  },
  { path: 'home', component: HomeComponent, canActivate: [PageGuard] },
  { path: '', pathMatch: "full", redirectTo: '/home' },
  {
    path: 'admin',
    loadChildren: () => import('./administration/administration.module').then(m => m.AdministrationModule)
  },
  { path: '', loadChildren: () => import('./checkout/checkout.module').then(m => m.CheckoutModule) },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    // enableTracing: !environment.production,
    scrollPositionRestoration: "top",
    preloadingStrategy: PreloadAllModules
  })],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule {
}
