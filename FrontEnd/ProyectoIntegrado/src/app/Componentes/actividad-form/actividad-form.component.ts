import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../Servicios/auth.service';
import { ActividadService } from '../../Servicios/actividad.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TipoActividadService } from '../../Servicios/tipo-actividad.service';

declare var google: any;

@Component({
  selector: 'app-actividad-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './actividad-form.component.html',
  styleUrls: ['./actividad-form.component.css']
})
export class ActividadFormComponent implements OnInit, AfterViewInit {
  actividad = {
    id: 0,
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
    idTipoActividad: 0
  };
  public listaTiposActividades: any[] = [];
  editando: boolean = false;
  idOfertante: any = 0;
  idActividad: any = 0;
  minFechaInicio: string = '';
  minFechaFin: string = '';
  map: any;
  marker: any;
  showOverlay: boolean = false;

  constructor(
    private authService: AuthService,
    private actividadService: ActividadService,
    private route: ActivatedRoute,
    private router: Router,
    private tiposActividadService: TipoActividadService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.idOfertante = params.get('idOfertante');
      this.idActividad = params.get('idActividad');
    });
    if (this.idActividad != 0) {
      this.editando = true;
    }
    this.checkLoginStatus();
    this.cargarActividad();
    this.cargarTiposActividades();
    this.setMinFechaInicio();
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  setMinFechaInicio(): void {
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const dd = String(today.getDate()).padStart(2, '0');
    const hh = String(today.getHours()).padStart(2, '0');
    const min = String(today.getMinutes()).padStart(2, '0');

    this.minFechaInicio = `${yyyy}-${mm}-${dd}T${hh}:${min}`;
  }

  camposCompletos(): boolean {
    return this.actividad.nombre != '' && this.actividad.descripcion != '' && this.actividad.fechaInicio != '' && this.actividad.numParticipantesTotal > 0 && this.actividad.numParticipantesTotal != null
      && this.actividad.fechaFin != '' && this.actividad.idTipoActividad != 0 && this.actividad.idTipoActividad != null;
  }

  onFechaInicioChange(event: any): void {
    this.actividad.fechaInicio = event.target.value;
    this.setMinFechaFin(this.actividad.fechaInicio);
  }

  onFechaFinChange(event: any): void {
    this.actividad.fechaFin = event.target.value;
  }

  setMinFechaFin(fechaInicio: string): void {
    this.minFechaFin = fechaInicio;
    if (this.actividad.fechaFin && new Date(this.actividad.fechaFin) < new Date(fechaInicio)) {
      this.actividad.fechaFin = '';
    }
  }


  convertToFront(date: string): string {
    const utcDate = new Date(date);
    const localDate = new Date(utcDate.getTime() + (2 * 60 * 60 * 1000));
    return localDate.toISOString().slice(0, 16); // Elimina los segundos y la 'Z'
  }

  cargarActividad(): void {
    if (this.editando) {
      this.showOverlay = true;
      this.actividadService.getById(this.idActividad).subscribe({
        next: (response) => {
          this.showOverlay = false;
          this.actividad = response;
          this.actividad.fechaInicio = this.convertToFront(this.actividad.fechaInicio);
          this.actividad.fechaFin = this.convertToFront(this.actividad.fechaFin);
          this.actividad.idOfertante = this.idOfertante;

          // Convertir la ubicación de cadena de texto a coordenadas y establecer en el mapa
          if (this.actividad.ubicacion) {
            const [lat, lng] = this.actividad.ubicacion.split(',').map(Number);
            this.setMapLocation(lat, lng);
          }
        },
        error: (error) => {
          this.showOverlay = false;
          console.error('Error', error);
        }
      });
    }
  }

  cargarTiposActividades(): void {
    this.showOverlay = true;
    this.tiposActividadService.listarTiposActividades().subscribe({
      next: (response) => {
        this.showOverlay = false;
        this.listaTiposActividades = response;
      },
      error: (error) => {
        this.showOverlay = false;
        console.error('Error', error);
        this.router.navigate(['/login']);
      }
    });
  }

  convertToBack(date: string): string {
    const localDate = new Date(date);
    const utcDate = new Date(localDate.getTime());
    return utcDate.toISOString().slice(0, 16); // Elimina los segundos y la 'Z'
  }

  addActividad(): void {
    if (new Date(this.actividad.fechaInicio) < new Date()) {
      alert('La fecha de inicio ya ha pasado 🫤');
      return;
    }

    if (new Date(this.actividad.fechaFin) <= new Date(this.actividad.fechaInicio)) {
      alert('La fecha de fin debe ser posterior a la fecha de inicio 🧐');
      return;
    }


    // Convertir a UTC antes de enviar
    const actividadCopy = { ...this.actividad };
    actividadCopy.fechaInicio = this.convertToBack(this.actividad.fechaInicio);
    actividadCopy.fechaFin = this.convertToBack(this.actividad.fechaFin);

    if (this.marker) {
      const lat = this.marker.getPosition().lat();
      const lng = this.marker.getPosition().lng();
      actividadCopy.ubicacion = `${lat},${lng}`;
    } else {
      alert('Por favor, selecciona una ubicación en el mapa 📌');
      return;
    }

    this.showOverlay = true;
    if (this.actividad.id != 0) {
      this.actividadService.put(actividadCopy).subscribe({
        next: (response) => {
          this.showOverlay = false;
          this.router.navigate(['/actividades']);
        },
        error: (error) => {
          this.showOverlay = false;
          alert(error.error + " 🫤");
        }
      });
    } else {
      this.actividadService.post(actividadCopy).subscribe({
        next: (response) => {
          this.showOverlay = false;
          this.router.navigate(['/actividades']);
        },
        error: (error) => {
          this.showOverlay = false;
          console.error('Error', error);
          this.router.navigate(['/login']);
        }
      });
    }
  }


  cancelar(): void {
    this.router.navigate([`/actividades`]);
  }

  checkLoginStatus(): void {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedToken: any = this.decodeToken(token);
      const currentTime = Math.floor(new Date().getTime() / 1000);
      if (decodedToken.exp && decodedToken.exp > currentTime) {
        // El token es válido
        this.idOfertante = decodedToken.idOfertante;
        this.actividad.idOfertante = decodedToken.idOfertante
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

  initMap(): void {
    const initialLocation = { lat: 37.38472635342472, lng: -5.970701321884839 }; // Ubicación inicial (El campo del más grande)
    this.map = new google.maps.Map(document.getElementById('map'), {
      center: initialLocation,
      zoom: 13
    });

    this.map.addListener('click', (event: any) => {
      this.setMarker(event.latLng);
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
  }

  setMapLocation(lat: number, lng: number): void {
    const location = { lat, lng };
    this.map.setCenter(location);
    this.setMarker(location);
  }


}
