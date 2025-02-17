package com.pzbapps.squiggly.common.presentation.textcolorsbottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import com.mohamedrejeb.richeditor.model.RichTextState
import com.pzbapps.squiggly.add_note_feature.domain.usecase.ColorList

@Composable
fun SingleRowTextColorList(
    colorList: ColorList,
    richTextState: MutableState<RichTextState>,
    showBottomSheet:MutableState<Boolean>
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(
                    CircleShape
                )
                .background(Color(colorList.rgb))
                .border(
                    width = 10.dp, // Border width
                    color = Color(colorList.rgb), // Border color
                    shape = CircleShape // Ensure the border follows a circular shape
                )
                .clickable {
                    richTextState.value.toggleSpanStyle(
                        spanStyle = SpanStyle(
                            color = Color(
                                colorList.rgb
                            )
                        )
                    )
                    showBottomSheet.value = false

                }

        ) {

        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(colorList.name, color = androidx.compose.material.MaterialTheme.colors.onPrimary)
        Spacer(modifier = Modifier.height(5.dp))
    }

}