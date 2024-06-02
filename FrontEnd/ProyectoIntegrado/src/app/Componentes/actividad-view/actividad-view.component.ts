import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../Servicios/auth.service';
import { ActividadService } from '../../Servicios/actividad.service';
import { TipoActividadService } from '../../Servicios/tipo-actividad.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OfertanteService } from '../../Servicios/ofertante.service';
import { GoogleMap } from '@angular/google-maps';
import { StarRatingComponent } from '../star-rating/star-rating.component';
import { ActividadConsumidorService } from '../../Servicios/actividad-consumidor.service';

declare var google: any;

@Component({
  selector: 'app-actividad-view',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, StarRatingComponent],
  templateUrl: './actividad-view.component.html',
  styleUrl: './actividad-view.component.css'
})
export class ActividadViewComponent implements OnInit, AfterViewInit {
  @Input()
  public id: any;

  actividad = {
    id: '',
    nombre: '',
    descripcion: '',
    fechaInicio: '',
    fechaFin: '',
    fechaCreacion: '',
    ubicacion: '',
    numParticipantes: 0,
    numParticipantesTotal: 0,
    valoracion: 0,
    idOfertante: 0,
    idTipoActividad: ''
  };
  ofertante = {
    id: '',
    usuario: '',
    nombre: '',
    apellidos: '',
    email: '',
    contrasena: '',
    foto: '',
    valoracion: '',
    descripcion: ''
  };
  tipoActividad = {
    id: '',
    tipo: ''
  };
  actividadConsumidor = {
    idActividad: '',
    idConsumidor: '',
    valoracion: 0
  };
  idConsumidor: any = 0;
  idOfertante: any = 0;
  isConsumidor: boolean = false;
  isOfertante: boolean = false;
  participa: boolean = false;
  map: any;
  marker: any;
  showOverlay: boolean = false;

  constructor(
    private authService: AuthService,
    private actividadService: ActividadService,
    private route: ActivatedRoute,
    private router: Router,
    private tiposActividadService: TipoActividadService,
    private ofertanteService: OfertanteService,
    private actividadConsumidorService: ActividadConsumidorService
  ) {
  }

  ngOnInit(): void {
    this.checkLoginStatus();
    this.cargarActividad();
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  cargarActividad(): void {
    this.showOverlay = true;
    this.actividadService.getById(this.id).subscribe({
      next: (response) => {
        this.showOverlay = false;
        console.log(response)
        this.actividad = response;
        if (this.actividad.ubicacion) {
          const [lat, lng] = this.actividad.ubicacion.split(',').map(Number);
          this.setMapLocation(lat, lng);
        }
        this.cargarTipoActividad();
        this.cargarOfertante();
        this.cargarActividadConsumidor();
      },
      error: (error) => {
        this.showOverlay = false;
        console.error('Error', error);
        this.router.navigate(['/login']);
      }
    });
  }

  cargarTipoActividad(): void {
    this.tiposActividadService.getById(this.actividad.idTipoActividad).subscribe({
      next: (response) => {
        this.tipoActividad = response;
      },
      error: (error) => {
        console.error('Error', error);
        this.router.navigate(['/login']);
      }
    });
  }

  cargarOfertante(): void {
    if (this.actividad.idOfertante) {
      this.ofertanteService.getById(this.actividad.idOfertante).subscribe({
        next: (response) => {
          this.ofertante = response;
        },
        error: (error) => {
          console.error('Error', error);
          this.router.navigate(['/login']);
        }
      });
    }
  }

  cargarActividadConsumidor(): void {
    this.actividadConsumidor.idActividad = this.actividad.id;
    this.actividadConsumidor.idConsumidor = this.idConsumidor;
    this.actividadConsumidorService.exist(this.actividadConsumidor).subscribe({
      next: (response) => {
        this.participa = response;
        if (this.participa) {
          this.actividadConsumidorService.getById(this.actividadConsumidor).subscribe({
            next: (response) => {
              this.actividadConsumidor = response
            },
            error: (error) => {
              console.error('Error', error);
              this.router.navigate(['/login']);
            }
          });
        }
      },
      error: (error) => {
        console.error('Error', error);
        this.router.navigate(['/login']);
      }
    });
  }

  guardarValoracion(valoracion: number): void {
    this.actividadConsumidor.valoracion = valoracion;
    //this.showOverlay = true;
    this.actividadConsumidorService.put(this.actividadConsumidor).subscribe({
      next: (response) => {
        this.showOverlay = false;
      },
      error: (error) => {
        this.showOverlay = false;
        console.error('Error', error);
        this.router.navigate(['/login']);
      }
    });
  }

  editarActividad() {
    this.router.navigate([`/actividad/edit/${this.idOfertante}/${this.actividad.id}`]);
  }

  borrarActividad() {
    this.showOverlay = true;
    this.actividadService.delete(this.actividad.id).subscribe({
      next: (response) => {
        this.showOverlay = false;
        const currentUrl = this.router.url;
        this.router.navigateByUrl('/home', { skipLocationChange: true }).then(() => {
          this.router.navigate([currentUrl]);
        });
      },
      error: (error) => {
        this.showOverlay = false;
        alert(error.error + " 🫤");
      }
    });
  }

  participarActividad() {
    this.showOverlay = true;
    this.actividadConsumidorService.post(this.actividadConsumidor).subscribe({
      next: (response) => {
        this.showOverlay = false;
        this.participa = true;
        this.cargarActividad()
      },
      error: (error) => {
        this.showOverlay = false;
        console.error('Error', error);
        this.router.navigate(['/login']);
      }
    });
  }

  dejarActividad() {
    this.showOverlay = true;
    this.actividadConsumidorService.delete(this.actividadConsumidor).subscribe({
      next: (response) => {
        this.showOverlay = false;
        this.participa = false;
        this.cargarActividad();
      },
      error: (error) => {
        this.showOverlay = false;
        console.error('Error', error);
        this.router.navigate(['/login']);
      }
    });
  }

  isPastDate(fecha: string): boolean {
    const today = new Date();
    const activityDate = new Date(fecha);
    return activityDate < today;
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
          this.idOfertante = decodedToken.idOfertante;
        }
        if (decodedToken.authorities.includes('CONSUMIDOR')) {
          this.isConsumidor = true;
          this.idConsumidor = decodedToken.idConsumidor;
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
      console.error('Error decoding token', error);
      return null;
    }
  }

  //MAP
  initMap(): void {
    const initialLocation = { lat: 37.38472635342472, lng: -5.970701321884839 }; // Ubicación inicial (El campo del más grande)
    this.map = new google.maps.Map(document.getElementById('map'), {
      center: initialLocation,
      zoom: 13
    });
  }

  setMarker(location: any): void {
    if (this.marker) {
      this.marker.setPosition(location);
    } else {
      this.marker = new google.maps.Marker({
        position: location,
        map: this.map
      });
    }
    console.log(this.marker)
  }

  setMapLocation(lat: number, lng: number): void {
    const location = { lat, lng };
    this.map.setCenter(location);
    this.setMarker(location);
  }
}
