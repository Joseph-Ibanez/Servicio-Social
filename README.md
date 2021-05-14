# Servicio-Social
## Aquí se manejaran todos los códigos, así como PDF's de avances sobre el proyecto. 
El proyecto en su parte inicial consiste en realizar un integrador, en este caso un Runge-Kutta de cuarto orden, para resolver la ecuación diferencial 
de primer orden que rige las trayectorias de órbitas círculares de objetos no masivos, fotones. El Runge-Kutta está en el archivo **RungeKutta.java**.

Una vez lograda la versión para el eje radial, se notó que para los mínimos la ecuación a integrar provocaba conflictos de signo, todo esto
está en el archivo **TrayectoriaFotones.java**, y se decidió cambiar a una EDO de segundo orden dada por: 

![equation](https://latex.codecogs.com/png.latex?u%27%27%20%3D%203mu%5E2-u)

con la cual no se sentirían dichos conflictos. 


La etapa de desarrollo consiste en crear un código que integre esta EDO mediante diferencias finitas, hasta aquí todavía no hay código. 

