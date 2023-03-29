import { ChangeDetectionStrategy, Component, ElementRef, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { faFacebookF, faGithub, faGoogle, faTwitter } from "@fortawesome/free-brands-svg-icons";
import { UserService } from "../../service/user.service";
import { Router } from "@angular/router";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { CookieService } from "ngx-cookie-service";
import { Store } from "@ngrx/store";
import { faEnvelope, faKey, faUser } from '@fortawesome/free-solid-svg-icons';
import { createFormData } from "../../service/service.index";
import { loginAction } from "../../store/action/auth.action";
import { NotifierService } from "angular-notifier";
import { NotificationType } from "../../enumeration/notification-enum";
import { resetPassword } from "../../store/action/user.action";
import { take } from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent implements OnInit {

  faFacebook = faFacebookF;
  faTwitter = faTwitter;
  faGoogle = faGoogle;
  faGithub = faGithub;
  faUser = faUser;
  faKey = faKey;
  faEnvelope = faEnvelope;

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(30)]),
    password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(50)])
  });

  resetPasswordForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email])
  });

  constructor(private userService: UserService, private router: Router,
              private cookieService: CookieService, private store: Store,
              private notifier: NotifierService) {
  }

  @ViewChild('passwordResetEmail') passwordResetEmail: ElementRef | undefined;
  @ViewChild('modalClose') modalClose: ElementRef | undefined;
  clientWidth: number = window.innerWidth;

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

    this.userService.obtainCsrf()
      .pipe(take(1)).subscribe();
  }

  public onLogin() {
    if (!this.username.value || !this.password.value) {
      return;
    }
    const formData: FormData = createFormData({
      'username': this.username.value,
      'password': this.password.value
    });

    this.store.dispatch(loginAction({ formData: formData }));
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get email() {
    return this.resetPasswordForm.get('email');
  }

  submitEmail() {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const email = this.passwordResetEmail.nativeElement.value;
    const isValidEmail = regex.test(email);

    if (!isValidEmail) {
      this.notifier.notify(NotificationType.INFO, 'The email address is invalid!');
      return;
    }

    this.store.dispatch(resetPassword({ email }));
    this.modalClose.nativeElement.click();
  }
}
