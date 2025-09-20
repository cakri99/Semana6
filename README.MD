# Agenda de Contactos (Java + SQLite)

Proyecto de **Agenda de Contactos** con interfaz gráfica en **Java Swing**, utilizando **SQLite** como base de datos.  
Permite:
- Guardar información personal (nombre, teléfono, correo).
- Listar y buscar contactos.
- Exportar contactos a CSV.

---

## Requisitos previos
- **Java JDK 8+** instalado  
  Verifica con:
  ```bash
  java --version

## Driver JDBC de SQLite:
- Descarga el .jar desde Xerial SQLite JDBC, Ejemplo: sqlite-jdbc-3.50.3.0.jar.
- Coloca el archivo .jar en el mismo directorio del proyecto.

## Ejecución del proyecto

Clonar el repositorio (Recomiendo esta opcion)
```bash
git clone https://github.com/cakri99/Semana6.git
cd AgendaContactos
```
Tambien puedes copiar los archivos en tu directorio.

# Compilar

Windows
```
javac -cp ".;sqlite-jdbc-3.50.3.0.jar" *.java
```

Linux/MacOs
```
javac -cp ".:sqlite-jdbc-3.50.3.0.jar" *.java
```

# Ejecutar

Windows
```
java -cp ".;sqlite-jdbc-3.50.3.0.jar" AgendaApp
```

Linus/MacOs
```
java -cp ".:sqlite-jdbc-3.50.3.0.jar" AgendaApp
```

# Descripcion de archivos
* AgendaApp.java → Interfaz gráfica principal.
* Contacto.java → Clase modelo de contacto.
* ContactoDAO.java → Acceso a datos (SQLite).
* ConexionDB.java → Manejo de la conexión a la base de datos.
* sqlite-jdbc-3.50.3.0.jar → Driver JDBC de SQLite.