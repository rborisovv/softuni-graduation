import {Component, OnDestroy, OnInit} from '@angular/core';
import {faFacebookF, faGithub, faGoogle, faTwitter} from "@fortawesome/free-brands-svg-icons";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CookieService} from "ngx-cookie-service";
import {Store} from "@ngrx/store";
import {loginAction} from "../../store/action/auth.action";
import {faUser, faKey} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

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
              private cookieService: CookieService, private readonly store: Store) {
  }

  ngOnInit(): void {
    const allShowHidePassword = document.querySelectorAll('.password-showHide')

    allShowHidePassword.forEach(item => {
      item.addEventListener('click', () => {
        item.classList.toggle('hide')
        if (item.closest('.form-input').querySelector('input').type === 'password') {
          item.closest('.form-input').querySelector('input').type = 'text'
        } else {
          item.closest('.form-input').querySelector('input').type = 'password'
        }
      })
    })
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
      next: (user) => {
        this.store.dispatch(loginAction({
          username: user.username, email: user.email
        }));

        if (user.role === 'ADMIN') {
          this.router.navigateByUrl('/admin/cockpit')
        } else {
          this.router.navigateByUrl('/home');
        }
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
