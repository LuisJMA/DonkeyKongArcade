# 🦍 Donkey Kong Arcade - Java

Proyecto recreativo inspirado en el clásico arcade de Donkey Kong, desarrollado en **Java** utilizando Programación Orientada a Objetos (POO) y componentes de Swing para la interfaz gráfica.

---

## 🎮 Características del Juego
* **Jugabilidad de plataformas**: Muévete, salta y esquiva los barriles rodantes en múltiples niveles.
* **Sistema de Vidas y Tiempo**: Control dinámico de tiempo límite y penalización por colisiones.
* **Ítems coleccionables**: Recolecta los elementos repartidos por las plataformas para alcanzar la victoria.
* **Gestión de Audio**: Efectos de sonido y música de fondo integrados mediante `SoundManager`.
* **Interfaz Gráfica fluida**: Renderizado optimizado con `JPanel`, `CardLayout` y control de ticks por segundo.

---

## 🛠️ Tecnologías y Requisitos
* **Lenguaje**: Java (JDK 8 o superior recomendado).
* **Interfaz**: Java Swing / AWT.
* **Control de versiones**: Git.

---

## 🚀 Cómo compilar y ejecutar el proyecto

Si estás compilando desde la terminal, sigue estos pasos:

mkdir bin

javac -d bin src\com\donkeykong\Main.java src\com\donkeykong\controller\*.java src\com\donkeykong\model\*.java src\com\donkeykong\view\*.java src\com\donkeykong\audio\*.java

java -cp bin com.donkeykong.Main

## 📁 Estructura del Proyecto

DonkeyKongArcade/
│
├── src/com/donkeykong/
│   ├── images/      # Recursos imágenes, sprites
│   ├── audio/       # Gestión de música y efectos de sonido
│   ├── controller/  # Controladores de eventos y teclado
│   ├── model/       # Clases lógicas (Player, Platform, Barrel, Item, etc.)
│   ├── view/        # Pantallas, menús y vistas (GamePanel, MainFrame)
│   └── Main.java    # Punto de entrada de la aplicación          
└── README.md
