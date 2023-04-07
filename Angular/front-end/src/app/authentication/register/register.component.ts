import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { faAddressCard, faCalendar, faEnvelope, faKey, faLock, faUser } from '@fortawesome/free-solid-svg-icons';
import { UserService } from "../../service/user.service";
import { map, Observable } from "rxjs";
import {
  AbstractControl,
  AsyncValidatorFn,
  FormControl,
  FormGroup,
  ValidationErrors,
  Validators
} from "@angular/forms";
import { DatePipe } from "@angular/common";
import { IUserRegisterModel } from "./IUserRegisterModel";
import { Store } from "@ngrx/store";
import { registerAction } from "../../store/action/auth.action";
import { checkPasswordEquality, passwordShowHideOption, removeListenersOnDestroy } from "../auth.index";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RegisterComponent implements OnInit, OnDestroy {

  faUser = faUser;
  faKey = faKey;
  faLock = faLock;
  faAddressCard = faAddressCard;
  faEnvelope = faEnvelope;
  faCalendar = faCalendar;

  constructor(private userService: UserService, private datePipe: DatePipe, private store: Store) {
  }

  registerForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(30)]),
    email: new FormControl('', [Validators.required, Validators.email],
      [EmailExistsValidator.validateEmail(this.userService)]),
    firstName: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]),
    lastName: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]),
    birthDate: new FormControl(undefined, [Validators.required]),
    password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]),
    confirmPassword: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(50)])
  });

  ngOnInit(): void {
    passwordShowHideOption();
  }

  public onRegister(): void {
    if (!this.username.value || !this.email.value || !this.firstName.value || !this.lastName.value
      || !this.birthDate.value || !this.password.value || !this.confirmPassword.value) {
      return;
    }

    if (!checkPasswordEquality(this.password.value, this.confirmPassword.value)) {
      return;
    }

    const registerData: IUserRegisterModel = {
      'username': this.username.value,
      'email': this.email.value,
      'firstName': this.firstName.value.toUpperCase().chatAt(0) + this.firstName.value.slice(1),
      'lastName': this.lastName.value.toUpperCase().chat(0) + this.firstName.value.slice(1),
      'birthDate': this.datePipe.transform(this.birthDate.value, 'dd-MM-yyyy'),
      'password': this.password.value,
      'confirmPassword': this.confirmPassword.value
    };

    this.store.dispatch(registerAction({ registerModel: registerData }));
  }

  public onConfirmPasswordChange(): boolean {
    return this.password.value === this.confirmPassword.value;
  }

  get today(): any {
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

  ngOnDestroy(): void {
    removeListenersOnDestroy();
  }
}

class EmailExistsValidator {
  static validateEmail(userService: UserService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return userService.isEmailPresent(control.value)
        .pipe(map((result: boolean) => result ? { emailPresent: true } : null));
    }
  }
}
