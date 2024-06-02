import { Component } from '@angular/core';
import { AuthService } from '../../Servicios/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActividadViewComponent } from '../actividad-view/actividad-view.component';
import { ActividadService } from '../../Servicios/actividad.service';
import { TipoActividadService } from '../../Servicios/tipo-actividad.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ActividadViewComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  public listaActividades: any[] = [];
  public listaActividadesConsumidor: any[] = [];
  isOfertante: boolean = false;
  isConsumidor: boolean = false;
  mostrarActividad: boolean = false;
  public idOfertanteLogueado: any = 0;
  public idConsumidorLogueado: any = 0;
  public idTipoActividad: any = null;
  public idActividad: any = null;
  decodedToken: any = null;
  currentPage: number = 1;
  currentPageConsumidor: number = 1;
  pageSize: number = 6;
  showOverlay: boolean = false;

  constructor(private authService: AuthService, private actividadService: ActividadService, private tiposActividadService: TipoActividadService, private router: Router) {
    this.checkLoginStatus();
    this.listarActividades();
    this.listarActividadesConsumidor();
  }

  listarActividades(): void {
    if (this.isOfertante) {
      this.showOverlay = true;
      this.actividadService.listarActividades(this.idTipoActividad, this.idOfertanteLogueado, '').subscribe({
        next: (response) => {
          this.showOverlay = false;
          this.listaActividades = response;
        },
        error: (error) => {
          this.showOverlay = false;
          this.router.navigate(['/login']);
        }
      });
    }
  }
  listarActividadesConsumidor(): void {
    if (this.isConsumidor) {
      this.showOverlay = true;
      this.actividadService.listarActividadesConsumidor(this.idConsumidorLogueado).subscribe({
        next: (response) => {
          this.showOverlay = false;
          this.listaActividadesConsumidor = response;
        },
        error: (error) => {
          this.showOverlay = false;
          this.router.navigate(['/login']);
        }
      });
    }
  }
  entrarActividad(x: any) {
    this.idActividad = x;
    this.mostrarActividad = true;
  }

  salirActividad() {
    this.mostrarActividad = false;
    this.listarActividades();
    this.listarActividadesConsumidor();
  }

  isPastDate(fechaInicio: string): boolean {
    const today = new Date();
    const activityDate = new Date(fechaInicio);
    return activityDate < today;
  }

  addActividad() {
    this.router.navigate([`/actividad/edit/${this.idOfertanteLogueado}/0`]);
  }

  //para actividades inscritas

  nextPageConsumidor(): void {
    if (this.currentPageConsumidor < this.totalPagesConsumidor()) {
      this.currentPageConsumidor++;
    }
  }

  previousPageConsumidor(): void {
    if (this.currentPageConsumidor > 1) {
      this.currentPageConsumidor--;
    }
  }

  firstPageConsumidor(): void {
    this.currentPageConsumidor = 1;
  }

  lastPageConsumidor(): void {
    this.currentPageConsumidor = this.totalPagesConsumidor();
  }

  totalPagesConsumidor(): number {
    return Math.ceil(this.listaActividadesConsumidor.length / this.pageSize);
  }

  getPaginatedActivitiesConsumidor(): any[] {
    const startIndex = (this.currentPageConsumidor - 1) * this.pageSize;
    return this.listaActividadesConsumidor.slice(startIndex, startIndex + this.pageSize);
  }

  //para actividades ofertadas

  nextPage(): void {
    if (this.currentPage < this.totalPages()) {
      this.currentPage++;
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  firstPage(): void {
    this.currentPage = 1;
  }

  lastPage(): void {
    this.currentPage = this.totalPages();
  }

  totalPages(): number {
    return Math.ceil(this.listaActividades.length / this.pageSize);
  }

  getPaginatedActivities(): any[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.listaActividades.slice(startIndex, startIndex + this.pageSize);
  }


  //JWT
  checkLoginStatus(): void {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedToken: any = this.decodeToken(token);
      const currentTime = Math.floor(new Date().getTime() / 1000);
      if (decodedToken.exp && decodedToken.exp > currentTime) {
        if (decodedToken.authorities.includes('OFERTANTE')) {
          this.isOfertante = true;
          this.idOfertanteLogueado = decodedToken.idOfertante;
        }
        if (decodedToken.authorities.includes('CONSUMIDOR')) {
          this.isConsumidor = true;
          this.idConsumidorLogueado = decodedToken.idConsumidor;
        }
      } else {
        this.authService.logout();
      }
    } else {
      this.authService.logout();
    }
  }

  decodeToken(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decodedPayload = atob(payload);
      return JSON.parse(decodedPayload);
    } catch (error) {
      return null;
    }
  }
}

