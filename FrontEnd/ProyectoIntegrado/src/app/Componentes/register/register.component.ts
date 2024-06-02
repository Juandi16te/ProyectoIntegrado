import { Component } from '@angular/core';
import { AuthService } from '../../Servicios/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  user = {
    usuario: '',
    nombre: '',
    apellidos: '',
    email: '',
    contrasena: '',
    foto: '',
    descripcion: ''
  };
  selectedRole: string = '';
  imagenSeleccionada: string | ArrayBuffer | null = null;
  showOverlay: boolean = false;
  constructor(private authService: AuthService, private router: Router) { }

  async cargarImagen(event: any) {
    const archivo = event.target.files[0];
    const extension = archivo.name.split('.').pop().toLowerCase(); // Obtener la extensión del archivo
    if (extension === 'jpg' || extension === 'jpeg' || extension === 'png' || extension === 'gif') {
      const reader = new FileReader();
      reader.readAsDataURL(archivo);
      reader.onload = () => {
        this.imagenSeleccionada = reader.result as string;
        this.user.foto = this.imagenSeleccionada; // Guardar la imagen en base64 en el objeto user
      };
    } else {
      // Mostrar mensaje de error o realizar alguna acción si el archivo no es una imagen
      alert('El archivo seleccionado no es una imagen 🧐');
      this.borrarImagen();
    }

  }

  borrarImagen() {
    this.user.foto = '';
    this.imagenSeleccionada = null;
    const inputArchivo = document.getElementById('foto') as HTMLInputElement;
    inputArchivo.value = ''; // Vaciar el valor del input de archivo
  }

  async register(): Promise<void> {
    if (this.selectedRole === 'ofertante') {
      this.showOverlay = true;
      this.authService.registerOfertante(this.user).subscribe({
        next: (response) => {
          this.router.navigate(['/login']);
          this.showOverlay = false;
        },
        error: (error) => {
          this.showOverlay = false;
          if (error.error === "existe usuario") {
            alert('Ya existe un usuario con ese nombre de usuario 😕');
          } else if (error.error === "existe email") {
            alert('Ya existe un usuario con ese email 😕');
          } else {
            console.error('Error registering user', error);
          }
        }
      });
    } else if (this.selectedRole === 'consumidor') {
      this.showOverlay = true;
      this.authService.registerConsumidor(this.user).subscribe({
        next: (response) => {
          this.showOverlay = false;
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.showOverlay = false;
          if (error.error === "existe usuario") {
            alert('Ya existe un usuario con ese nombre de usuario 😕');
          } else if (error.error === "existe email") {
            alert('Ya existe un usuario con ese email 😕');
          } else {
            console.error('Error registering user', error);
          }
        }
      });
    } else if (this.selectedRole === 'ambos') {
      this.showOverlay = true;
      this.authService.registerOfertanteConsumidor(this.user).subscribe({
        next: (response) => {
          this.showOverlay = false;
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.showOverlay = false;
          if (error.error === "existe usuario") {
            alert('Ya existe un usuario con ese nombre de usuario 😕');
          } else if (error.error === "existe email") {
            alert('Ya existe un usuario con ese email 😕');
          } else {
            console.error('Error registering user', error);
          }
        }
      });
    }
  }
}