package com.hackclub.molten.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.hackclub.molten.ApprovalState
import com.hackclub.molten.Project
import com.hackclub.molten.Review
import com.hackclub.molten.theming.WavyShape
import com.hackclub.molten.theming.getContentCardColors
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import moltenplatform.app.shared.generated.resources.Res
import moltenplatform.app.shared.generated.resources.molten_progress_volcano
import org.jetbrains.compose.resources.painterResource


@Composable
fun ProjectCard(project: Project? = null) {
    var project = project
    if (project == null) {
        project = Project(
            id = "placeholderproject",
            userId = "placeholderuser",
            title = "Placeholder Project!",
            description = "If you can see this and you're a normal user, something went wrong, please complain in #magma-help. This project was thoughtfully crafted to be a beautiful placeholder for testing purposes during development of the Magma platform, and I'm writing a nice long description to make sure it can handle text overflows nicely ",
            demoUrl = "https://github.com/GingerGigiCat/molten",
            repoUrl = "https://example.com",
            readmeUrl = "https://raw.githubusercontent.com/GingerGigiCat/Molten/refs/heads/master/README.md",
            approvalState = ApprovalState.DraftState,
            workedHours = 33.4f,
            approvedHours = 10.1f,
            deniedHours = 2f,
            reviews = listOf(
                Review(
                    id="placeholderreview",
                    projectId = "placeholderproject",
                    externalComment = "awesome project! i really like the part where it's a placeholder",
                    internalComment = "it's a placeholder :( but shhhh be nice to them",
                    timestamp = "1970-01-01T00:00:00Z",
                )),
            headerImgUrl = "https://cdn.hackclub.com/01a0b850-0f9c-7bfe-bb43-2d521796fa6e/screenshot_2026-09-19_at_11.41.45.png",
            screenshotImgUrl = "https://cdn.hackclub.com/01a0b850-0f9c-7bfe-bb43-2d521796fa6e/screenshot_2026-09-19_at_11.41.45.png"
        )
    }
    Card(Modifier.height(400.dp).padding(15.dp),
        shape = WavyShape(),
        colors = getContentCardColors(MaterialTheme.colorScheme)) {
        Column {
            KamelImage({asyncPainterResource(project.headerImgUrl)}, contentDescription = "Header Image", modifier = Modifier.requiredHeight(200.dp))
        }
    }
}

@Composable
fun ProjectsPage() {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 400.dp)){
        items(10) {
            ProjectCard()
        }
    }
}

