import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private readonly store: Store, private http: HttpClient) {
  }

  ngOnInit(): void {

  }
}
