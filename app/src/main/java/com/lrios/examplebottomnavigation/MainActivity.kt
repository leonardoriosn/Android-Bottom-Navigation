package com.lrios.examplebottomnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lrios.examplebottomnavigation.ui.theme.ExampleBottomNavigationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExampleBottomNavigationTheme {
                val navigationController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    bottomBar = { MenuBottomNavigation(navController = navigationController) },
                    floatingActionButton = { MyFloatingActionBar(snackbarHostState) },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    floatingActionButtonPosition = FabPosition.End,
                ) { innerPadding ->

                    NavigationGraph(
                        navController = navigationController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuBottomNavigation(navController: NavController) {
    val pantallas = listOf(
        Routes.PantallaInicio,
        Routes.PantallaUsuarios,
        Routes.PantallaFavoritos
    )

    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Blue,
    ){
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Blue
            //colocalr los iconos color blanco

        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            pantallas.forEach { pantalla ->
                NavigationBarItem(
                    selected = currentRoute == pantalla.route,
                    onClick = {
                        // Evita duplicar destinos en el backstack
                        navController.navigate(pantalla.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = pantalla.icon),
                            contentDescription = pantalla.titulo
                        )
                    },
                    label = { Text(text = pantalla.titulo) },
                    alwaysShowLabel = false, // En Material3 ahora es un parámetro opcional
                    colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,    // icono seleccionado blanco
                    unselectedIconColor = Color.White,  // icono no seleccionado blanco
                    selectedTextColor = Color.White,    // texto seleccionado blanco
                    unselectedTextColor = Color.White,  // texto no seleccionado blanco
                    indicatorColor = Color.Blue  // color del "pill" de selección (azul más oscuro)
                )
                )
            }
        }
    }

}

@Composable
fun NavigationGraph(
    navController: androidx.navigation.NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "inicio",
        modifier = modifier
    ) {
        composable("inicio")     { PantallaInicio() }
        composable("usuarios")   { PantallaUsuarios() }
        composable("favoritos")  { PantallaFavoritos() }
    }
}

@Composable
fun MyFloatingActionBar(snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Floating Action Button Clicked",
                    actionLabel = "Aceptar",
                    withDismissAction = true,
                    duration = SnackbarDuration.Indefinite
                )
            }
        },
        containerColor = Color.Blue,   // 🔵 color de fondo
        contentColor = Color.White     // ⚪ color del ícono
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Agregar",

        )
    }
}