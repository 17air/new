// app/src/main/java/com/example/cardify/ui/components/PrimaryButton.kt
package com.example.cardify.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.ui.theme.Gray
import com.example.cardify.ui.theme.OffWhite
import com.example.cardify.ui.theme.PrimaryTeal

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 56.dp,
    textSize: Int = 16,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp), // More rounded corners
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryTeal,
            contentColor = OffWhite,
            disabledContainerColor = Gray, // Lighter teal for disabled state
            disabledContentColor = OffWhite.copy(alpha = 0.7f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 4.dp,
            disabledElevation = 0.dp
        ),
        border = null,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = OffWhite,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                fontSize = textSize.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}