package com.example.recipe2.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun CircularProgressBar(
    isDisplayed: Boolean,
    modifier: Modifier = Modifier
) {
    if (isDisplayed) {


        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (progressBar, text) = createRefs()
            //val guideline = createGuidelineFromTop(0.3f)


            CircularProgressIndicator(
                modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                color = MaterialTheme.colors.primary
            )


            Text(
                modifier = Modifier
                    .constrainAs(text) {
                        top.linkTo(progressBar.bottom)
                        start.linkTo(progressBar.start)
                        end.linkTo(progressBar.end)
                    }
                    .padding(top = 6.dp),
                text = "Loading...",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp
                )
            )

        }


    }
}