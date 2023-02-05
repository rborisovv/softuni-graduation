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
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {createFormData} from "../../service/service.index";
import {Subscription, take} from "rxjs";
import {CategoryUpdate} from "../../interface/category.update";
import {Store} from "@ngrx/store";
import {updateCategoryAction} from "../../store/action/category.action";

@Component({
  selector: 'app-update-categories',
  templateUrl: './update-category.component.html',
  styleUrls: ['./update-category.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UpdateCategoryComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  private oldName: string = '';
  private oldCategoryIdentifier: string = '';

  constructor(private categoryService: CategoryService, private router: Router, private notifier: NotifierService,
              private activatedRoute: ActivatedRoute, private changeDetectorRef: ChangeDetectorRef,
              private readonly store: Store) {
  }

  ngOnInit(): void {
    let identifier: string = '';
    const routerSubscription = this.activatedRoute.paramMap.pipe(take(1)).subscribe((params: ParamMap) => {
      identifier = params.get('identifier');
    });
    this.oldCategoryIdentifier = identifier;

    const subscription = this.categoryService.loadCategory(identifier)
      .subscribe({
        next: (response) => {
          this.oldName = response.name;
          this.setName(response.name);
          this.setIdentifier(response.identifier);
          this.setSuperCategoryIdentifier(response.superCategoryIdentifier);
          this.imageSrc = response.mediaUrl;
          this.changeDetectorRef.markForCheck();
        }
      });

    this.subscriptions.push(routerSubscription);
    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
  }

  updateCategoryFormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(5),
      Validators.maxLength(40)]),
    identifier: new FormControl('', [Validators.required,
      Validators.minLength(4), Validators.maxLength(10)]),
    superCategoryIdentifier: new FormControl('', [Validators.maxLength(30)]),
    media: new FormControl('')
  });

  @ViewChild('media') mediaInput: ElementRef;

  mediaPath: string;
  imageSrc: string;


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
        this.imageSrc = reader.result as string;
        this.changeDetectorRef.markForCheck();
      };

      reader.readAsDataURL(file);
    }
  }

  updateCategory(): void {
    const categoryData: CategoryUpdate = {
      name: this.name.value,
      oldName: this.oldName,
      identifier: this.identifier.value,
      oldCategoryIdentifier: this.oldCategoryIdentifier,
      superCategoryIdentifier: this.superCategoryIdentifier.value,
      media: this.mediaInput.nativeElement.files[0]
    }

    this.store.dispatch(updateCategoryAction({formData: createFormData(categoryData)}));
  }

  get name() {
    return this.updateCategoryFormGroup.get('name');
  }

  get identifier() {
    return this.updateCategoryFormGroup.get('identifier');
  }

  get superCategoryIdentifier() {
    return this.updateCategoryFormGroup.get('superCategoryIdentifier');
  }

  get media() {
    return this.updateCategoryFormGroup.get('media');
  }

  public setName(name: string) {
    this.updateCategoryFormGroup.controls['name'].setValue(name);
  }

  public setIdentifier(identifier: string) {
    this.updateCategoryFormGroup.controls['identifier'].setValue(identifier);
  }

  public setSuperCategoryIdentifier(superCategoryIdentifier: string) {
    this.updateCategoryFormGroup.controls['superCategoryIdentifier'].setValue(superCategoryIdentifier);
  }
}

//TODO: Create validations for user input (Lower or Uppercase)//
