import { NgModule } from '@angular/core';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {UserService} from "../service/user.service";
import {CookieService} from "ngx-cookie-service";
import {RouterLink} from "@angular/router";


@NgModule({
    declarations: [
        HeaderComponent,
        FooterComponent
    ],
    imports: [
        FontAwesomeModule,
        RouterLink
    ],
  exports: [
    HeaderComponent
  ],
  providers: [
    UserService, CookieService
  ]
})
export class SharedModule { }
