package com.example.proyecto1ppm.data

object SuggestionRepository {
    val suggestions = listOf(
        // Sugerencias para estudiar solo
        Suggestion(
            id = 1,
            title = "Establece un horario de estudio",
            description = "Crea un horario regular para estudiar y apégate a él para formar un hábito.",
            type = SuggestionType.SOLO
        ),
        Suggestion(
            id = 2,
            title = "Elige un lugar adecuado",
            description = "Encuentra un lugar tranquilo y sin distracciones para concentrarte mejor.",
            type = SuggestionType.SOLO
        ),
        // ... (agrega las 8 sugerencias restantes para estudiar solo)
        Suggestion(
            id = 10,
            title = "Utiliza técnicas de memorización",
            description = "Aplica técnicas como mapas mentales o mnemotécnicas para recordar información.",
            type = SuggestionType.SOLO
        ),
        // Sugerencias para estudiar en grupo
        Suggestion(
            id = 11,
            title = "Forma un grupo de estudio",
            description = "Reúnete con compañeros que tengan objetivos similares para estudiar juntos.",
            type = SuggestionType.GROUP
        ),
        Suggestion(
            id = 12,
            title = "Asigna roles en el grupo",
            description = "Divide responsabilidades para que cada miembro aporte al estudio colectivo.",
            type = SuggestionType.GROUP
        ),
        // ... (agrega las 8 sugerencias restantes para estudiar en grupo)
        Suggestion(
            id = 20,
            title = "Organiza sesiones de preguntas y respuestas",
            description = "Cada miembro prepara preguntas para los demás, promoviendo el aprendizaje activo.",
            type = SuggestionType.GROUP
        )
    )
}
