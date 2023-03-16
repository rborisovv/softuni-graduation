import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { RouterModule, Routes } from "@angular/router";
import { ReactiveFormsModule } from "@angular/forms";
import { UserService } from "../service/user.service";
import { SharedModule as SharedModule } from '../shared/shared.module';
import { CookieService } from "ngx-cookie-service";
import { AuthGuard } from "../guard/auth.guard";
import { EffectsModule } from "@ngrx/effects";
import { AuthEffects } from "../store/effect/auth.effect";
import { MatInputModule } from "@angular/material/input";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MAT_DATE_LOCALE, MatNativeDateModule } from "@angular/material/core";

const routes: Routes = [
  {
    path: '', canActivate: [AuthGuard], children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent }
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
    SharedModule,
    EffectsModule.forFeature(AuthEffects),
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  providers: [UserService, DatePipe, CookieService,
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' }
  ],
  exports: [LoginComponent, RegisterComponent]
})

export class AuthenticationModule {
}
