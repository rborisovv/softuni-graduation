import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef, OnDestroy,
  ViewChild,
  ViewChildren,
  ViewEncapsulation
} from '@angular/core';
import {CategoryService} from "../../service/category.service";
import {Router} from "@angular/router";
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";
import {AbstractControl, AsyncValidatorFn, FormControl, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {catchError, map, Observable, Subscription} from "rxjs";
import {MediaService} from "../../service/media.service";
import {Media} from "../../interface/media";
import {Category} from "../../interface/category";
import {createFormData} from "../../service/service.index";
import {CategorySharedFunctionality} from "../item.category.index";

@Component({
  selector: 'app-create-categories',
  templateUrl: './create-category.component.html',
  styleUrls: ['./create-category.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class CreateCategoryComponent extends CategorySharedFunctionality implements OnDestroy {
  constructor(private categoryService: CategoryService, private router: Router,
              private notifier: NotifierService, private mediaService: MediaService,
              protected override changeDetectorRef: ChangeDetectorRef) {
    super(changeDetectorRef);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
  }

  createCategoryFormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(4),
      Validators.maxLength(40)], [nameValidator.validateName(this.categoryService)]),
    identifier: new FormControl('', [Validators.required,
      Validators.minLength(4), Validators.maxLength(10)], identifierValidator.validateIdentifier(this.categoryService)),
    productNamePrefix: new FormControl('', [Validators.maxLength(30)]),
    media: new FormControl(undefined),
    pkOfFile: new FormControl('')
  });

  @ViewChild('mediaSearchInput') mediaSearchInput: ElementRef;
  @ViewChild('previewCategoryMedia') previewCategoryMedia: ElementRef;
  @ViewChildren('row') modalMediaRows: ElementRef[];

  filteredMedias$: Observable<Media[]>;
  selectedMediaFromPickup: Media;

  subscriptions: Subscription[] = [];


  public filterMediaByName(): void {
    const mediaSearchInputElement = this.mediaSearchInput.nativeElement;
    if (mediaSearchInputElement.value.trim() === '') {
      return;
    }
    this.filteredMedias$ = this.mediaService.filterMediaByName(mediaSearchInputElement.value);
  }

  createCategory(): void {
    if (!this.mediaInput.nativeElement.files[0] && this.pkOfFIle.value === '') {
      this.notifier.notify(NotificationType.INFO, "Please select a Category media!");
      return;
    }

    const categoryData: Category = {
      name: this.name.value.trim(),
      identifier: this.identifier.value.trim(),
      productNamePrefix: this.productNamePrefix.value.trim(),
      media: this.mediaInput.nativeElement.files[0],
      pkOfFile: this.pkOfFIle.value
    }

    const subscription = this.categoryService.createCategory(createFormData(categoryData))
      .pipe(
        catchError((error) => {
          this.notifier.notify(NotificationType.ERROR, error.error.message);
          throw error;
        })
      )
      .subscribe({
        next: (response) => {
          this.router.navigateByUrl('/admin/cockpit').then(() => {
            this.notifier.notify(NotificationType.SUCCESS, `${response.message}`);
          });
        }
      });

    this.subscriptions.push(subscription);
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

  get pkOfFIle() {
    return this.createCategoryFormGroup.get('pkOfFile');
  }

  protected override onMediaUpload(event: any) {
    super.onMediaUpload(event);
    this.selectedMediaFromPickup = null;
  }

  public selectCategoryMedia(selectedRow: HTMLTableRowElement, media: Media): void {
    this.modalMediaRows.forEach(x => {
      x.nativeElement.closest('tr').classList.remove('selected-media');
    });

    (selectedRow).classList.add('selected-media');
    this.selectedMediaFromPickup = media;
  }

  public submitMediaSelection(): void {
    this.previewCategoryMedia.nativeElement.src = this.selectedMediaFromPickup.mediaUrl;
    this.mediaPath = this.selectedMediaFromPickup.mediaUrl;
    document.getElementById('modal-close-btn').click();

    this.pkOfFIle.setValue(this.selectedMediaFromPickup.pkOfFile);
    this.media.setValue('');
    this.mediaInput.nativeElement.value = '';
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
