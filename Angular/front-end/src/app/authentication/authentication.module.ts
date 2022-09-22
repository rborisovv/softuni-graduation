import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
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
    FormsModule
  ],
  providers: [UserService],
  exports: [LoginComponent, RegisterComponent]
})
export class AuthenticationModule {
}
