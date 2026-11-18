# Enrutamiento avanzado en Android: entrega

## Ejercicios de ampliación realizados

### Ejercicio 1: segundo argumento (prioridad)
- `fragment_first.xml`: nuevo `EditText` numérico `etPrioridad` (1 a 3).
- `FirstFragment.java`: se valida que la prioridad esté entre 1 y 3 (si no, aparece un Toast) y se envía con
  `FirstFragmentDirections.actionFirstFragmentToSecondFragment(textoNota).setPrioridad(prioridad)`.
- `nav_graph.xml`: argumento `prioridad` de tipo `integer` en `SecondFragment`.
- `SecondFragment.java` / `fragment_second.xml`: se muestra «Prioridad: N» en `textview_prioridad`.
- Decisión: `prioridad` tiene `android:defaultValue="0"`. Si fuera obligatoria (sin valor por defecto),
  SafeArgs la pediría como segundo parámetro del método `actionFirstFragmentToSecondFragment(String, int)`,
  pero entonces el deep link `notas://detalle/{tituloNota}` dejaría de funcionar, porque no la lleva en la URI.
  Con el valor por defecto, el método solo exige `tituloNota` y la prioridad se pasa con `setPrioridad()`.
  Si llega 0 (por ejemplo, al entrar por el deep link), la pantalla 2 oculta la prioridad.

### Ejercicio 3: tercera pantalla (Confirmación)
- Nuevos `ThirdFragment.java` y `fragment_third.xml`.
- `nav_graph.xml`: destino `ThirdFragment` con el argumento `tituloNota`, la acción
  `action_SecondFragment_to_ThirdFragment` y la acción `action_ThirdFragment_to_FirstFragment` con
  `app:popUpTo="@id/FirstFragment"` y `app:popUpToInclusive="true"`.
- En la pantalla 2, el botón «Confirmar nota» navega a la pantalla 3 pasando el título.
- En la pantalla 3, el botón «Volver al inicio» vacía la pila hasta FirstFragment, incluida, y crea una
  pantalla 1 nueva. La pila queda `[1]` y no `[1, 2, 3, 1]`. Si después pulsas Atrás, la app se cierra.

## Preguntas de comprobación

**1. ¿Qué diferencia hay entre una Activity y un Fragment en esta app?**
Solo hay una Activity (`MainActivity`). Es la ventana que contiene la barra superior y el `NavHostFragment`.
Los Fragments (`FirstFragment`, `SecondFragment` y `ThirdFragment`) son las pantallas. Cada uno tiene su layout y
su ciclo de vida, y se van cambiando dentro de ese hueco sin crear Activities nuevas.

**2. ¿Qué tres elementos puede contener el `<fragment>` de un grafo?**
- `<action>`: una ruta hacia otro destino. Ejemplo: `<action android:id="@+id/action_FirstFragment_to_SecondFragment" app:destination="@id/SecondFragment" />`
- `<argument>`: un dato que recibe la pantalla. Ejemplo: `<argument android:name="tituloNota" app:argType="string" />`
- `<deepLink>`: una URI externa que abre la pantalla. Ejemplo: `<deepLink app:uri="notas://detalle/{tituloNota}" />`

**3. ¿Qué clases genera SafeArgs y quién las escribe?**
Genera `FirstFragmentDirections`, `SecondFragmentDirections` y `ThirdFragmentDirections` (un método por cada
acción que sale del destino) y `SecondFragmentArgs` y `ThirdFragmentArgs` (getters de los argumentos que recibe
cada pantalla). Las escribe el plugin de Gradle SafeArgs a partir de `nav_graph.xml` en cada compilación.
Nosotros no las escribimos ni debemos editarlas.

**4. ¿Por qué es más seguro `args.getTituloNota()` que `getArguments().getString("tituloNota")`?**
Con `getString("tituloNota")` la clave es un texto escrito a mano. Si tiene una errata, el código compila igual
y en ejecución se recibe `null`. `getTituloNota()` lo genera el plugin desde el grafo: no hay claves escritas a
mano, el tipo (`String`) está garantizado y cualquier error se detecta al compilar.

**5. ¿Qué ocurre con la pila si usas `navigate()` para volver? ¿Y con `popBackStack()`?**
`navigate()` apila una pantalla 1 nueva encima: tras ir y volver varias veces, la pila queda 1-2-1-2-1… y el
botón Atrás recorre todas esas pantallas. `popBackStack()` quita la pantalla de arriba y vuelve a mostrar la
pantalla 1 que ya estaba debajo, con su texto. La pila no crece.

**6. Explica cada parte de `notas://detalle/{tituloNota}`.**
- `notas://`: el esquema, propio de nuestra app (igual que `https://` en la web).
- `detalle`: el host, que identifica la sección de la app a la que va el enlace.
- `/{tituloNota}`: un marcador. Lo que haya en esa posición de la URI se convierte en el argumento
  `tituloNota` del destino, y `SecondFragmentArgs` lo recibe igual que si viniera del botón.

## Prueba del deep link (PowerShell)
```
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" shell am start -a android.intent.action.VIEW -d "notas://detalle/Comprar%20pan"
```
