import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./core/home/home.component";
import {environment} from "../environments/environment";

const routes: Routes = [
  {path: 'auth', loadChildren: () => import('./authentication/authentication.module').then(m => m.AuthenticationModule)},
  {path: 'home', component: HomeComponent},
  {path: '', pathMatch: "full", redirectTo: '/home'},
  {
    path: 'admin',
    loadChildren: () => import('./administration/administration.module').then(m => m.AdministrationModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: !environment.production})],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule {
}
