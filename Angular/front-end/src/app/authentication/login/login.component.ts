import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {faFacebookF, faGithub, faGoogle, faTwitter} from "@fortawesome/free-brands-svg-icons";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CookieService} from "ngx-cookie-service";
import {Store} from "@ngrx/store";
import {faKey, faUser} from '@fortawesome/free-solid-svg-icons';
import {createFormData} from "../../service/service.index";
import {loginAction} from "../../store/action/auth.action";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent implements OnInit {

  faFacebook = faFacebookF;
  faTwitter = faTwitter;
  faGoogle = faGoogle;
  faGithub = faGithub;
  faUser = faUser;
  faKey = faKey;

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]),
    password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(50)])
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
    //TODO: Remove the event listeners manually
  }

  public onLogin() {
    if (!this.username.value || !this.password.value) {
      return;
    }
    const formData: FormData = createFormData({
      'username': this.username.value,
      'password': this.password.value
    });

    this.store.dispatch(loginAction({formData: formData}));
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }
}
