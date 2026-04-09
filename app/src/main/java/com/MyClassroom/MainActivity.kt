package com.MyClassroom

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

// ─────────────────────────────────────────────
// Modelo de datos: representa un estudiante
// ─────────────────────────────────────────────
data class Estudiante(
    val nombre: String,
    val jornada: String,
    val esBecado: Boolean,
    val nota1: Double,
    val nota2: Double,
    val nota3: Double,
    val nota4: Double
) {
    // Porcentajes: N1=20%, N2=30%, N3=15%, N4=35%
    val notaDefinitiva: Double
        get() = (nota1 * 0.20) + (nota2 * 0.30) + (nota3 * 0.15) + (nota4 * 0.35)

    // Pierde si nota definitiva es menor a 3.0
    val perdio: Boolean
        get() = notaDefinitiva < 3.0
}

class MainActivity : AppCompatActivity() {

    // Lista acumulada de estudiantes ingresados
    private val listaEstudiantes = mutableListOf<Estudiante>()

    // Referencias a los componentes de la UI
    private lateinit var etNombre: EditText
    private lateinit var rgJornada: RadioGroup
    private lateinit var cbBecado: CheckBox
    private lateinit var etNota1: EditText
    private lateinit var etNota2: EditText
    private lateinit var etNota3: EditText
    private lateinit var etNota4: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnCalcular: Button
    private lateinit var btnLimpiar: Button
    private lateinit var tvResultados: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar componentes
        inicializarVistas()

        // Configurar listeners de botones
        btnAgregar.setOnClickListener { agregarEstudiante() }
        btnCalcular.setOnClickListener { mostrarResultados() }
        btnLimpiar.setOnClickListener { limpiarTodo() }
    }

    // ─────────────────────────────────────────────
    // Enlaza las variables con los IDs del layout
    // ─────────────────────────────────────────────
    private fun inicializarVistas() {
        etNombre    = findViewById(R.id.etNombre)
        rgJornada   = findViewById(R.id.rgJornada)
        cbBecado    = findViewById(R.id.cbBecado)
        etNota1     = findViewById(R.id.etNota1)
        etNota2     = findViewById(R.id.etNota2)
        etNota3     = findViewById(R.id.etNota3)
        etNota4     = findViewById(R.id.etNota4)
        btnAgregar  = findViewById(R.id.btnAgregar)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnLimpiar  = findViewById(R.id.btnLimpiar)
        tvResultados = findViewById(R.id.tvResultados)
    }

    // ─────────────────────────────────────────────
    // Valida y agrega un estudiante a la lista
    // ─────────────────────────────────────────────
    private fun agregarEstudiante() {
        val nombre = etNombre.text.toString().trim()

        // Validar nombre
        if (nombre.isEmpty()) {
            mostrarError("El nombre del estudiante no puede estar vacío.")
            etNombre.requestFocus()
            return
        }

        // Leer jornada del RadioGroup
        val jornadaId = rgJornada.checkedRadioButtonId
        val jornada = when (jornadaId) {
            R.id.rbManana -> "Mañana"
            R.id.rbTarde  -> "Tarde"
            R.id.rbNoche  -> "Noche"
            else          -> "No seleccionada"
        }

        val esBecado = cbBecado.isChecked

        // Validar y parsear las 4 notas
        val nota1 = validarNota(etNota1, "Nota 1") ?: return
        val nota2 = validarNota(etNota2, "Nota 2") ?: return
        val nota3 = validarNota(etNota3, "Nota 3") ?: return
        val nota4 = validarNota(etNota4, "Nota 4") ?: return

        // Crear estudiante y agregarlo a la lista
        val estudiante = Estudiante(nombre, jornada, esBecado, nota1, nota2, nota3, nota4)
        listaEstudiantes.add(estudiante)

        // Mostrar confirmación en pantalla
        val msg = "✅ Estudiante #${listaEstudiantes.size} agregado: $nombre"
        tvResultados.setTextColor(Color.parseColor("#2E7D32"))
        tvResultados.text = msg

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

        // Limpiar solo el formulario, no la lista
        limpiarFormulario()
    }

    // ─────────────────────────────────────────────
    // Valida que una nota sea Double entre 1.0 y 5.0
    // Retorna null si hay error (y muestra el mensaje)
    // ─────────────────────────────────────────────
    private fun validarNota(campo: EditText, nombreNota: String): Double? {
        val texto = campo.text.toString().trim()

        // Campo vacío
        if (texto.isEmpty()) {
            mostrarError("$nombreNota no puede estar vacía.")
            campo.requestFocus()
            return null
        }

        // No es número
        val valor = texto.toDoubleOrNull()
        if (valor == null) {
            mostrarError("$nombreNota debe ser un número válido (Ej: 3.5).")
            campo.requestFocus()
            return null
        }

        // Fuera del rango 1.0 – 5.0
        if (valor < 1.0 || valor > 5.0) {
            mostrarError("$nombreNota debe estar entre 1.0 y 5.0. Valor ingresado: $valor")
            campo.setText("")
            campo.requestFocus()
            return null
        }

        return valor
    }

    // ─────────────────────────────────────────────
    // Calcula y muestra los resultados de todos
    // los estudiantes registrados
    // ─────────────────────────────────────────────
    private fun mostrarResultados() {
        if (listaEstudiantes.isEmpty()) {
            mostrarError("No hay estudiantes registrados. Agrega al menos uno.")
            return
        }

        val sb = StringBuilder()
        var estudiantesPerdieron = 0

        sb.appendLine("════════════════════════════════")
        sb.appendLine("  RESULTADOS FINALES")
        sb.appendLine("  Total de estudiantes: ${listaEstudiantes.size}")
        sb.appendLine("════════════════════════════════\n")

        listaEstudiantes.forEachIndexed { index, est ->
            val definitiva = String.format("%.2f", est.notaDefinitiva)
            val estado = if (est.perdio) {
                estudiantesPerdieron++
                "❌ REPROBÓ"
            } else {
                "✅ APROBÓ"
            }
            val becado = if (est.esBecado) " 🎓 (Becado)" else ""

            sb.appendLine("📌 Estudiante ${index + 1}: ${est.nombre}$becado")
            sb.appendLine("   Jornada  : ${est.jornada}")
            sb.appendLine("   N1=${est.nota1} | N2=${est.nota2} | N3=${est.nota3} | N4=${est.nota4}")
            sb.appendLine("   Definitiva: $definitiva  →  $estado")
            sb.appendLine()
        }

        sb.appendLine("════════════════════════════════")

        // Resumen final
        val aprobaron = listaEstudiantes.size - estudiantesPerdieron
        sb.appendLine("📊 RESUMEN:")
        sb.appendLine("   ✅ Aprobaron  : $aprobaron estudiante(s)")
        sb.appendLine("   ❌ Reprobaron : $estudiantesPerdieron estudiante(s)")

        val porcentajePerdida = if (listaEstudiantes.isNotEmpty()) {
            String.format("%.1f", (estudiantesPerdieron.toDouble() / listaEstudiantes.size) * 100)
        } else "0.0"
        sb.appendLine("   📉 % Reprueba : $porcentajePerdida%")
        sb.appendLine("════════════════════════════════")

        tvResultados.text = sb.toString()
        tvResultados.setTextColor(Color.parseColor("#1A237E"))

        // Mostrar Toast especial si todos perdieron o nadie perdió
        when {
            estudiantesPerdieron == listaEstudiantes.size ->
                Toast.makeText(this, "⚠️ Todos los estudiantes reprobaron.", Toast.LENGTH_LONG).show()
            estudiantesPerdieron == 0 ->
                Toast.makeText(this, "🎉 ¡Todos los estudiantes aprobaron!", Toast.LENGTH_LONG).show()
            else ->
                Toast.makeText(this, "$estudiantesPerdieron estudiante(s) reprobaron.", Toast.LENGTH_SHORT).show()
        }
    }

    // ─────────────────────────────────────────────
    // Limpia solo los campos del formulario
    // (no borra la lista de estudiantes)
    // ─────────────────────────────────────────────
    private fun limpiarFormulario() {
        etNombre.setText("")
        etNota1.setText("")
        etNota2.setText("")
        etNota3.setText("")
        etNota4.setText("")
        rgJornada.check(R.id.rbManana)
        cbBecado.isChecked = false
        etNombre.requestFocus()
    }

    // ─────────────────────────────────────────────
    // Limpia TODO: formulario + lista + resultados
    // ─────────────────────────────────────────────
    private fun limpiarTodo() {
        listaEstudiantes.clear()
        limpiarFormulario()
        tvResultados.text = "Aún no hay datos. Agrega estudiantes y presiona Calcular."
        tvResultados.setTextColor(Color.parseColor("#546E7A"))
        Toast.makeText(this, "🗑️ Datos borrados correctamente.", Toast.LENGTH_SHORT).show()
    }

    // ─────────────────────────────────────────────
    // Muestra un mensaje de error usando Toast
    // ─────────────────────────────────────────────
    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, "⚠️ $mensaje", Toast.LENGTH_LONG).show()
    }
}