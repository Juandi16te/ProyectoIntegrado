import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-star-rating',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './star-rating.component.html',
  styleUrl: './star-rating.component.css'
})
export class StarRatingComponent {
  @Input() rating: number = 0;
  @Input() puedeValorar: boolean = true; // Nuevo Input para el booleano
  @Output() ratingChange: EventEmitter<number> = new EventEmitter<number>();

  stars: number[] = [1, 2, 3, 4, 5];
  starTypes: string[] = [];

  ngOnChanges(): void {
    this.updateStarTypes();
  }

  rate(star: number) {
    if (this.puedeValorar) {
      this.rating = star;
      this.ratingChange.emit(this.rating);
      this.updateStarTypes();
    }
  }

  updateStarTypes() {
    this.starTypes = this.stars.map(star => {
      if (this.rating >= star) {
        return 'full';
      } else if (this.rating >= star - 0.5) {
        return 'half';
      } else {
        return 'empty';
      }
    });
  }

  getStarType(index: number): string {
    return this.starTypes[index];
  }
}
