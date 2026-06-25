package com.muhiqbal.moviedb

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.muhiqbal.moviedb.core.ui.theme.BaseTheme
import com.muhiqbal.moviedb.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MyMovieApp()
        }
    }
}

@Composable
fun MyMovieApp(
    modifier: Modifier = Modifier,
) {
    BaseTheme {
        AppNavigation(modifier = modifier)
    }
}