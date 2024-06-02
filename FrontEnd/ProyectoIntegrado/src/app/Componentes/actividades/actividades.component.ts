import { Component } from '@angular/core';
import { ActividadService } from '../../Servicios/actividad.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../Servicios/auth.service';
import { TipoActividadService } from '../../Servicios/tipo-actividad.service';
import { ActividadViewComponent } from '../actividad-view/actividad-view.component';

@Component({
  selector: 'app-actividades',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ActividadViewComponent],
  templateUrl: './actividades.component.html',
  styleUrl: './actividades.component.css'
})
export class ActividadesComponent {
  public listaActividades: any[] = [];
  public listaTiposActividades: any[] = [];
  isOfertante: boolean = false;
  mostrarActividad: boolean = false;
  public idOfertanteLogueado: any = 0;
  public idTipoActividad: any = null;
  public idOfertante: any = null;
  public idActividad: any = null;
  public nombre: string = '';
  decodedToken: any = null;
  currentPage: number = 1;
  pageSize: number = 6;
  showOverlay: boolean = false;

  constructor(private authService: AuthService, private actividadService: ActividadService, private tiposActividadService: TipoActividadService, private router: Router) {
    this.checkLoginStatus();
    this.listarTodo();
  }


  listarTodo() {
    this.showOverlay = true;
    this.actividadService.listarActividades(this.idTipoActividad, this.idOfertante, this.nombre).subscribe({
      next: (response) => {
        this.listaActividades = response;
        this.listarTiposActividades();
        this.showOverlay = false;
      },
      error: (error) => {
        this.showOverlay = false;
        this.router.navigate(['/login']);
      }
    });
  }
  listarActividades(): void {
    this.actividadService.listarActividades(this.idTipoActividad, this.idOfertante, this.nombre).subscribe({
      next: (response) => {
        this.listaActividades = response;
      },
      error: (error) => {
        this.router.navigate(['/login']);
      }
    });
  }

  listarTiposActividades(): void {
    this.showOverlay = true;
    this.tiposActividadService.listarTiposActividades().subscribe({
      next: (response) => {
        this.showOverlay = false
        this.listaTiposActividades = response;
      },
      error: (error) => {
        this.showOverlay = false
        this.router.navigate(['/login']);
      }
    });
  }

  buscarActividad(): void {
    this.listarActividades();
    this.currentPage = 1;
  }

  filtrarTipo(event: any): void {
    this.idTipoActividad = event.target.value;
    this.listarActividades();
    this.currentPage = 1;
  }

  addActividad() {
    this.router.navigate([`/actividad/edit/${this.idOfertanteLogueado}/0`]);
  }

  entrarActividad(x: any) {
    this.idActividad = x;
    this.mostrarActividad = true;
  }

  salirActividad() {
    this.mostrarActividad = false;
    this.listarActividades();
  }

  isPastDate(fechaInicio: string): boolean {
    const today = new Date();
    const activityDate = new Date(fechaInicio);
    return activityDate < today;
  }

  getPaginatedActivities(): any[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.listaActividades.slice(startIndex, startIndex + this.pageSize);
  }

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

