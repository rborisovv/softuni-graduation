import { NgModule } from '@angular/core';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {UserService} from "../service/user.service";
import {CookieService} from "ngx-cookie-service";
import {RouterLink, RouterLinkActive} from "@angular/router";
import { AuthHeaderComponent } from './auth-header/auth-header.component';


@NgModule({
    declarations: [
        HeaderComponent,
        FooterComponent,
        AuthHeaderComponent
    ],
  imports: [
    FontAwesomeModule,
    RouterLink,
    RouterLinkActive
  ],
  exports: [
    HeaderComponent,
    AuthHeaderComponent
  ],
  providers: [
    UserService, CookieService
  ]
})
export class SharedModule { }
