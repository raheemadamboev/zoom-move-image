package xyz.teamgravity.zoommoveimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import xyz.teamgravity.zoommoveimage.ui.theme.ZoomMoveImageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZoomMoveImageTheme {
                var scale by remember { mutableFloatStateOf(1F) }
                var rotation by remember { mutableFloatStateOf(1F) }
                var offset by remember { mutableStateOf(Offset.Zero) }

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(736F / 451F)
                ) {
                    val state = rememberTransformableState(
                        onTransformation = { zoomChange, panChange, rotationChange ->
                            scale = (scale * zoomChange).coerceIn(
                                minimumValue = 1F,
                                maximumValue = 5F
                            )

                            rotation += rotationChange

                            val extraWidth = (scale - 1) * constraints.maxWidth
                            val extraHeight = (scale - 1) * constraints.maxHeight

                            val maxX = extraWidth / 2
                            val maxY = extraHeight / 2

                            offset = Offset(
                                x = (offset.x + scale * panChange.x).coerceIn(
                                    minimumValue = -maxX,
                                    maximumValue = maxX
                                ),
                                y = (offset.y + scale * panChange.y).coerceIn(
                                    minimumValue = -maxY,
                                    maximumValue = maxY
                                )
                            )
                        }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                rotationZ = rotation
                                translationX = offset.x
                                translationY = offset.y
                            }
                            .transformable(state)
                    )
                }
            }
        }
    }
}
