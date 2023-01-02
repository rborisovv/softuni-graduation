import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {faFacebookF, faGithub, faGoogle, faTwitter} from "@fortawesome/free-brands-svg-icons";
import {faCalendar, faKey, faLock, faEnvelope, faAddressCard, faUser} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../service/user.service";
import {map, Observable, Subscription, switchMap, tap} from "rxjs";
import {
  AbstractControl,
  AsyncValidator, AsyncValidatorFn,
  EmailValidator,
  FormControl,
  FormGroup,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {DatePipe} from "@angular/common";
import {environment} from "../../../environments/environment.prod";
import {Router} from "@angular/router";
import {IUserRegisterModel} from "./IUserRegisterModel";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {
  url: string = '/auth/register';
  private subscriptions: Subscription[] = [];
  private apiHost = environment.apiHost;


  faFacebook = faFacebookF;
  faTwitter = faTwitter;
  faGoogle = faGoogle;
  faGithub = faGithub;
  faUser = faUser;
  faKey = faKey;
  faLock = faLock;
  faAddressCard = faAddressCard;
  faEnvelope = faEnvelope;
  faCalendar = faCalendar;

  constructor(private userService: UserService, private datePipe: DatePipe, private router: Router) {
  }

  registerForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]),
    email: new FormControl('', [Validators.required, Validators.email],
      [EmailExistsValidator.validateEmail(this.userService)]),
    firstName: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]),
    lastName: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]),
    birthDate: new FormControl(undefined, [Validators.required]),
    password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]),
    confirmPassword: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(50)])
  });

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
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  public onRegister(): void {
    if (!this.username.value || !this.email.value || !this.firstName.value || !this.lastName.value
      || !this.birthDate.value || !this.password.value || !this.confirmPassword.value) {
      return;
    }

    const registerData: IUserRegisterModel = {
      'username': this.username.value,
      'email': this.email.value,
      'firstName': this.firstName.value,
      'lastName': this.lastName.value,
      'birthDate': this.datePipe.transform(this.birthDate.value, 'dd-MM-yyyy'),
      'password': this.password.value,
      'confirmPassword': this.confirmPassword.value
    };

    const subscription: Subscription = this.userService.registerUser(registerData).subscribe({
      next: () => {
        this.router.navigateByUrl('/login');
      }
    });

    this.subscriptions.push(subscription);
  }

  public onConfirmPasswordChange(): boolean {
    return this.password.value === this.confirmPassword.value;
  }

  get today() {
    let today = new Date();
    return this.datePipe.transform(today, 'yyyy-MM-dd');
  }

  get username(): AbstractControl {
    return this.registerForm.get('username');
  }

  get email(): AbstractControl {
    return this.registerForm.get('email');
  }

  get firstName(): AbstractControl {
    return this.registerForm.get('firstName');
  }

  get lastName(): AbstractControl {
    return this.registerForm.get('lastName');
  }

  get birthDate(): AbstractControl {
    return this.registerForm.get('birthDate');
  }

  get password(): AbstractControl {
    return this.registerForm.get('password');
  }

  get confirmPassword(): AbstractControl {
    return this.registerForm.get('confirmPassword');
  }
}

class EmailExistsValidator {
  static validateEmail(userService: UserService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return userService.isEmailPresent(control.value)
        .pipe(map((result: boolean) => result ? {emailPresent: true} : null));
    }
  }
}
