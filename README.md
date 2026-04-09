📚 MyClassroom
Aplicación Android para el control y cálculo de notas académicas. Permite registrar múltiples estudiantes con sus cuatro calificaciones ponderadas, validar los datos ingresados y generar un resumen de resultados con estadísticas de aprobación y reprobación.

📸 Vista previa

<img width="365" height="712" alt="image" src="https://github.com/user-attachments/assets/2fbf7017-006f-4b1d-977b-9a60d1f587b7" />



✨ Funcionalidades

Registro de estudiantes con nombre, jornada (Mañana / Tarde / Noche) y estado de beca
Ingreso de 4 notas con pesos porcentuales distintos: N1 (20%), N2 (30%), N3 (15%), N4 (35%)
Validación de datos en tiempo real: campos vacíos, valores no numéricos y rango 1.0 – 5.0
Cálculo automático de nota definitiva ponderada
Indicador de aprobación o reprobación por estudiante (umbral: 3.0)
Resumen final con conteo de aprobados, reprobados y porcentaje de reprobación
Limpieza de formulario sin perder la lista acumulada
Limpieza total de todos los datos registrados


🧮 Fórmula de cálculo
Nota Definitiva = (N1 × 0.20) + (N2 × 0.30) + (N3 × 0.15) + (N4 × 0.35)
El estudiante aprueba si su nota definitiva es ≥ 3.0.
