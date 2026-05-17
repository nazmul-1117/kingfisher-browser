import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit
) {

    val animatedScale by animateFloatAsState(
        targetValue = if (isBookmarked) 1.15f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "bookmark_scale"
    )

    val gradient = remember {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF6A1B9A),
                Color(0xFF8E24AA)
            )
        )
    }

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .scale(animatedScale)
            .clip(CircleShape)
            .background(gradient)
    ) {
        Icon(
            imageVector = if (isBookmarked)
                Icons.Rounded.Bookmark
            else
                Icons.Rounded.BookmarkBorder,

            contentDescription = if (isBookmarked)
                "Remove Bookmark"
            else
                "Add Bookmark",

            tint = Color.White
        )
    }
}