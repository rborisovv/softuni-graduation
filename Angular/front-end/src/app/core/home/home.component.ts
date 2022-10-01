import {Component, OnInit} from '@angular/core';
import {CityService} from "../../service/city.service";
import {City} from "../../interface/city";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  public cities: City[];

  constructor(private cityService: CityService) {
  }

  ngOnInit(): void {
    this.getCity();
  }

  private getCity(): void {
    this.cityService.findAllCities().subscribe({
      next: (cities) => {
        this.cities = cities;
      }
    })
  }
}
