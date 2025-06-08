package com.example.cardify.ui.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cardify.R
import com.example.cardify.ui.theme.PrimaryTeal
import androidx.compose.foundation.layout.Row

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExistingScreen(
    navController: NavController,
    onImageSelected: (Uri) -> Unit = {}
) {
    val context = LocalContext.current
    
    // Launchers for different image selection methods
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // Handle captured image
        // Note: For actual implementation, you'll need to save the bitmap and get its URI
        Toast.makeText(context, "Camera functionality will be implemented", Toast.LENGTH_SHORT).show()
    }
    
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.firstOrNull()?.let { onImageSelected(it) }
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, proceed with the action
        } else {
            Toast.makeText(context, "Permission required to access files", Toast.LENGTH_SHORT).show()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Add Existing Business Card",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title
            Text(
                text = "How would you like to add\nyour business card?",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Option 1: Take Photo
            OptionCard(
                iconResId = R.drawable.ic_camera, // You'll need to add this drawable
                title = "Take Photo",
                subtitle = "Take a photo of a business card"
            ) {
                permissionLauncher.launch(Manifest.permission.CAMERA)
                // cameraLauncher.launch() // Uncomment when implementing camera
                Toast.makeText(context, "Camera functionality will be implemented", Toast.LENGTH_SHORT).show()
            }
            
            // Option 2: Select from Gallery
            OptionCard(
                iconResId = R.drawable.ic_gallery, // You'll need to add this drawable
                title = "Select from Gallery",
                subtitle = "Choose a business card from your gallery"
            ) {
                galleryLauncher.launch("image/*")
            }
            
            // Option 3: Select from Files
            OptionCard(
                iconResId = R.drawable.ic_folder, // You'll need to add this drawable
                title = "Select from Files",
                subtitle = "Choose a business card from your files"
            ) {
                fileLauncher.launch("image/*")
            }
            
            // Image preview (initially hidden)
            // Add this when implementing image preview
        }
    }
}

@Composable
private fun OptionCard(
    iconResId: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(PrimaryTeal.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // Text
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    )
                }
                
                // Arrow
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right), // Add this drawable
                    contentDescription = "Go",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp)
                )
            }
        }
    }
}

// Add this to your strings.xml:
/*
<resources>
    <string name="add_existing_title">Add Existing Business Card</string>
    <string name="how_to_add">How would you like to add\nyour business card?</string>
    <string name="take_photo">Take Photo</string>
    <string name="take_photo_desc">Take a photo of a business card</string>
    <string name="select_gallery">Select from Gallery</string>
    <string name="select_gallery_desc">Choose a business card from your gallery</string>
    <string name="select_files">Select from Files</string>
    <string name="select_files_desc">Choose a business card from your files</string>
</resources>
*/

// Add these drawables to your drawable folder:
/*
- ic_camera.xml
- ic_gallery.xml
- ic_folder.xml
- ic_arrow_right.xml
*/
