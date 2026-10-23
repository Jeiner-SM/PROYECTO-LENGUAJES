import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from '../../shared/components/ui/header/header';
import { Footer } from '../../shared/components/ui/footer/footer';


@Component({
  selector: 'app-public-layout',
  imports: [RouterOutlet,Header,Footer],
  templateUrl: './public-layout.html',
  styleUrl: './public-layout.scss',
})
export class PublicLayout {

}
