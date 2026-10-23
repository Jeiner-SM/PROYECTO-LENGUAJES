import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from "../../shared/components/ui/sidebar/sidebar";

// El cierre de sesión real vive en Sidebar.logout() (es el botón que usa el template).
// Este método quedó sin uso y se retiró para no dejar dos implementaciones divergentes.
@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    Sidebar
],
  templateUrl: './dashboard-layout.html',
  styleUrl: './dashboard-layout.scss'
})
export class DashboardLayout {}