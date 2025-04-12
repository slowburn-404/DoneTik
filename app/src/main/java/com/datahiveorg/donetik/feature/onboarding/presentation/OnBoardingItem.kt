package com.datahiveorg.donetik.feature.onboarding.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OnBoardingItem(
    modifier: Modifier = Modifier,
    pagerItem: PagerItems,
    context: Context
) {
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Image(
            modifier = Modifier.padding(vertical = 16.dp),
            painter = painterResource(pagerItem.imageId),
            contentDescription = null
        )

        Text(
            text = context.getString(pagerItem.title),
            textAlign = TextAlign.Center,
            style = typography.headlineMedium
        )

        Text(
            text = context.getString(pagerItem.description),
            textAlign = TextAlign.Center,
            style = typography.bodyMedium
        )

    }

}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun OnBoardingItemPreview() {
//    MaterialTheme {
//        OnBoardingItem(
//            pagerItem = PagerItems.Todo
//        )
//    }
//}