import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {CategoryService} from "../../service/category.service";
import {Category} from "../../interface/category";
import {Router} from "@angular/router";
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";
import {createFormData} from "../../service/service.index";
import {AbstractControl, AsyncValidatorFn, FormControl, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {map, Observable} from "rxjs";
import {MediaService} from "../../service/media.service";
import {Media} from "../../interface/media";

@Component({
  selector: 'app-create-categories',
  templateUrl: './create-category.component.html',
  styleUrls: ['./create-category.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class CreateCategoryComponent {
  constructor(private categoryService: CategoryService, private router: Router,
              private notifier: NotifierService, private mediaService: MediaService,
              private changeDetectorRef: ChangeDetectorRef) {
  }

  createCategoryFormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(4),
      Validators.maxLength(40)], [nameValidator.validateName(this.categoryService)]),
    identifier: new FormControl('', [Validators.required,
      Validators.minLength(4), Validators.maxLength(10)], identifierValidator.validateIdentifier(this.categoryService)),
    productNamePrefix: new FormControl('', [Validators.maxLength(30)]),
    media: new FormControl('', Validators.required)
  });

  @ViewChild('mediaInput') mediaInput: ElementRef;
  @ViewChild('mediaSearchInput') mediaSearchInput: ElementRef;

  mediaPath: string;
  imageSrc: string | ArrayBuffer;
  filteredMedias$: Observable<Media[]>;


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
      reader.onload = () => {
        this.imageSrc = reader.result;
        this.changeDetectorRef.markForCheck();
      };

      reader.readAsDataURL(file);
    }
  }


  public filterMediaByName(): void {
    const mediaSearchInputElement = this.mediaSearchInput.nativeElement;
    if (mediaSearchInputElement.value.trim() === '') {
      return;
    }
      this.filteredMedias$ = this.mediaService.filterMediaByName(mediaSearchInputElement.value);
  }

  createCategory(): void {
    const categoryData: Category = {
      name: this.name.value,
      identifier: this.identifier.value,
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
