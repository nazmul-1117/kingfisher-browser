import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AiOrbButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val orbGradient = remember {
        Brush.radialGradient(
            colors = listOf(
                Color(0xFF00E5FF),
                Color(0xFF6A1B9A)
            )
        )
    }

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(58.dp)
            .shadow(
                elevation = 20.dp,
                shape = CircleShape,
                ambientColor = Color(0xFF00E5FF),
                spotColor = Color(0xFF8E24AA)
            )
            .clip(CircleShape)
            .background(orbGradient)
    ) {
        Icon(
            imageVector = Icons.Rounded.AutoAwesome,
            contentDescription = "AI Assistant",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}