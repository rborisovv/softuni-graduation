import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegisterComponent} from './authentication/register/register.component';
import {LoginComponent} from "./authentication/login/login.component";
import {HomeComponent} from "./core/home/home.component";
import {AuthGuard} from "./guard/auth.guard";

const routes: Routes = [
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: '', pathMatch: "full", redirectTo: '/home'},
  {
    path: 'admin',
    loadChildren: () => import('./administration/administration.module').then(m => m.AdministrationModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthGuard]
})
export class AppRoutingModule {
}
