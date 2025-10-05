/*package com.example.grupoclouds  // Asegúrate de que el paquete sea el tuyo

data class Miembro(    val nombre: String,
                       val id: String,
                       val urlFoto: String // Usaremos una URL para la foto, Glide se encargará de cargarla
)*/

package com.example.grupoclouds

// Esta data class define la estructura de un miembro
data class Miembro(
    val nombre: String,
    val idSocio: String,
    val urlImagen: String // Usaremos una URL para la imagen de perfil
)
