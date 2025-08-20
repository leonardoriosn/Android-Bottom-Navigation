package com.lrios.examplebottomnavigation

sealed class Routes (val titulo: String, val icon: Int, val route: String) {
    object PantallaInicio: Routes("inicio", R.drawable.icon_home, "inicio")
    object PantallaUsuarios: Routes("usuarios", R.drawable.icon_user, "usuarios")
    object PantallaFavoritos: Routes("favoritos", R.drawable.icon_favorite, "favoritos")
}