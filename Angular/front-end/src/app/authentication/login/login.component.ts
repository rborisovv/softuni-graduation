import {Component, OnDestroy, OnInit} from '@angular/core';
import {faFacebookF, faGithub, faGoogle, faTwitter} from "@fortawesome/free-brands-svg-icons";
import {faUser} from '@fortawesome/free-regular-svg-icons';
import {faKey} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {NotificationType} from "../../enumeration/notification-enum";
import {NotifierService} from "angular-notifier";
import {CookieService} from "ngx-cookie-service";
import {JwtHelperService} from "@auth0/angular-jwt";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];

  private jwtCookieName: string = 'X-XSRF-TOKEN';

  faFacebook = faFacebookF;
  faTwitter = faTwitter;
  faGoogle = faGoogle;
  faGithub = faGithub;
  faUser = faUser;
  faKey = faKey;

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]),
    password: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(50)])
  });

  constructor(private userService: UserService, private router: Router,
              private readonly notifier: NotifierService,
              private cookieService: CookieService, private jwtService: JwtHelperService) {
  }

  ngOnInit(): void {
    const CsrfTokenCookie = this.cookieService.get(this.jwtCookieName);
    if (CsrfTokenCookie) {
      this.router.navigateByUrl('/home');
    }
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  public onLogin() {
    if (!this.username.value || !this.password.value) {
      return;
    }
    const formData: FormData = this.userService.createFormData({
      'username': this.username.value,
      'password': this.password.value
    });
    const subscription: Subscription = this.userService.loginUser(formData).subscribe({
      next: () => {
        this.router.navigateByUrl('/home');
        const jwtCookie = this.cookieService.get(this.jwtCookieName);
        const subject: string = this.jwtService.decodeToken(jwtCookie).sub;
        this.notifier.notify(NotificationType.SUCCESS, `Welcome, ${subject}`);
      }
    });

    this.subscriptions.push(subscription);
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }
}
