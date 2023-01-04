import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef, OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {CategoryService} from "../../service/category.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {NotifierService} from "angular-notifier";
import {AbstractControl, AsyncValidatorFn, FormControl, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {Category} from "../../interface/category";
import {createFormData} from "../../service/service.index";
import {NotificationType} from "../../enumeration/notification-enum";
import {map, Observable, Subscription} from "rxjs";

@Component({
  selector: 'app-update-category',
  templateUrl: './update-category.component.html',
  styleUrls: ['./update-category.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UpdateCategoryComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];

  constructor(private categoryService: CategoryService, private router: Router, private notifier: NotifierService,
              private activatedRoute: ActivatedRoute, private changeDetectionRef: ChangeDetectorRef) {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
  }

  ngOnInit(): void {
    let identifier: string = '';
    const routerSubscription = this.activatedRoute.paramMap.subscribe((params: ParamMap) => {
      identifier = params.get('identifier');
    });

    const subscription = this.categoryService.loadCategory(identifier)
      .subscribe({
        next: (response) => {
          this.setName(response.name);
          this.setIdentifier(response.categoryIdentifier);
          this.setProductPrefix(response.productNamePrefix);
          this.imageSrc = response.mediaUrl;
          this.changeDetectionRef.markForCheck();
        }
      });

    this.subscriptions.push(routerSubscription);
    this.subscriptions.push(subscription);
  }

  createCategoryFormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(5),
      Validators.maxLength(40)], [nameValidator.validateName(this.categoryService)]),
    identifier: new FormControl('', [Validators.required,
      Validators.minLength(4), Validators.maxLength(10)], identifierValidator.validateIdentifier(this.categoryService)),
    productNamePrefix: new FormControl('', [Validators.maxLength(30)]),
    media: new FormControl('')
  });

  @ViewChild('media') mediaInput: ElementRef;

  mediaPath: string;
  imageSrc: string | ArrayBuffer;


  public handleMediaUploadClick(): void {
    this.mediaInput.nativeElement.click();
  }

  public onMediaUpload(event: any) {
    this.mediaPath = this.mediaInput.nativeElement.value;
    this.readURL(event);
  }

  readURL(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = () => this.imageSrc = reader.result;

      reader.readAsDataURL(file);
    }
  }

  createCategory(): void {
    const categoryData: Category = {
      name: this.name.value,
      categoryIdentifier: this.identifier.value,
      productNamePrefix: this.productNamePrefix.value,
      media: this.mediaInput.nativeElement.files[0]
    }

    this.categoryService.createCategory(createFormData(categoryData))
      .subscribe({
        next: (response) => {
          this.router.navigateByUrl('/admin/cockpit').then(() => {
            this.notifier.notify(NotificationType.SUCCESS, `${response.message}`);
          });
        }
      });
  }

  get name() {
    return this.createCategoryFormGroup.get('name');
  }

  get identifier() {
    return this.createCategoryFormGroup.get('identifier');
  }

  get productNamePrefix() {
    return this.createCategoryFormGroup.get('productNamePrefix');
  }

  get media() {
    return this.createCategoryFormGroup.get('media');
  }

  public setName(name: string) {
    this.createCategoryFormGroup.controls['name'].setValue(name);
  }

  public setIdentifier(identifier: string) {
    this.createCategoryFormGroup.controls['identifier'].setValue(identifier);
  }

  public setProductPrefix(productPrefix: string) {
    this.createCategoryFormGroup.controls['productNamePrefix'].setValue(productPrefix);
  }
}

class identifierValidator {
  static validateIdentifier(categoryService: CategoryService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return categoryService.isCategoryByIdentifierPresent(control.value)
        .pipe(map((result: boolean) => result ? {identifierPresent: true} : null));
    }
  }
}

class nameValidator {
  static validateName(categoryService: CategoryService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return categoryService.isCategoryByNamePresent(control.value)
        .pipe(map((result: boolean) => result ? {namePresent: true} : null));
    }
  }
}
