import {NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {RouterModule} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {UserService} from "../service/user.service";


@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent
  ],
  imports: [
    CommonModule,
    FontAwesomeModule,
    RouterModule,
    ReactiveFormsModule
  ],
  providers: [UserService, DatePipe],
  exports: [LoginComponent, RegisterComponent]
})
export class AuthenticationModule {
}
