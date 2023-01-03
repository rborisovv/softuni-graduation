import {NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {RouterModule, Routes} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {UserService} from "../service/user.service";
import {SharedModule as SharedModule} from '../shared/shared.module';
import {CookieService} from "ngx-cookie-service";
import {AuthGuard} from "../guard/auth.guard";

const routes: Routes = [
  {
    path: '', canActivate: [AuthGuard], children: [
      {path: 'login', component: LoginComponent},
      {path: 'register', component: RegisterComponent}
    ]
  }
]

@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent
  ],
  imports: [
    CommonModule,
    FontAwesomeModule,
    RouterModule.forChild(routes),
    ReactiveFormsModule,
    SharedModule
  ],
  providers: [UserService, DatePipe, CookieService],
  exports: [LoginComponent, RegisterComponent]
})
export class AuthenticationModule {
}
